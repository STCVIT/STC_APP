package com.mstc.mstcapp.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mstc.mstcapp.MainActivity;
import com.mstc.mstcapp.R;

import java.util.Map;

public class FirebaseNotificationService extends FirebaseMessagingService {

    private static final String TAG = "NotificationService";
    private static final String EVENT_CHANNEL_ID = "EventNotifications";
    private static final String OTHER_CHANNEL_ID = "OtherNotifications";

    public FirebaseNotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createEventNotificationChannel();
        createOtherNotificationChannel();
        //FOR WAKE LOCK
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        boolean isScreenOn = pm.isInteractive(); // check if screen is on
        if (!isScreenOn) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myApp:notificationLock");
            wl.acquire(3000);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("event")
                .addOnCompleteListener(task -> {
                    String msg = "Subscribed";
                    if (!task.isSuccessful()) {
                        msg = "Could not subscribe";
                    }
                    Log.d(TAG, msg);
                });
        FirebaseMessaging.getInstance().subscribeToTopic("other")
                .addOnCompleteListener(task -> {
                    String msg = "Subscribed";
                    if (!task.isSuccessful()) {
                        msg = "Could not subscribe";
                    }
                    Log.d(TAG, msg);
                });

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //ONLY WHEN APP IS IN FOREGROUND
        Log.i(TAG, "onMessageReceived: " + remoteMessage.getData());
        //onMessageReceived: {startDate=14, description=1, title=10}
        Log.i(TAG, "onMessageReceived: " + remoteMessage.getFrom());
        if (remoteMessage.getFrom().equalsIgnoreCase("/topics/event")) {
            Map<String, String> map = remoteMessage.getData();
            String title = map.get("title") + " on " + map.get("startDate");
            String description = map.get("description");
            assert title != null;
            sendTopicNotification(title, description);
        } else if (remoteMessage.getFrom().equalsIgnoreCase("/topics/other")) {
            Map<String, String> map = remoteMessage.getData();
            String title = map.get("title") + " on " + map.get("startDate");
            String description = map.get("description");
            assert title != null;
            sendOtherNotification(title, description);
        }
    }

    @Override
    public void onDeletedMessages() {

    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token : " + token);
    }

    @SuppressLint("InvalidWakeLockTag")
    private void sendTopicNotification(String title, String messageBody) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, EVENT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stc_white)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.log_bg_color))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(0, notificationBuilder.build());

    }

    @SuppressLint("InvalidWakeLockTag")
    private void sendOtherNotification(String title, String messageBody) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, OTHER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stc_white)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.log_bg_color))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(0, notificationBuilder.build());

    }

    //ANDROID 8.0 AND ABOVE
    private void createEventNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Event Notifications";
            String description = "Notify about upcoming events";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(EVENT_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createOtherNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Other Notifications";
            String description = "Notify about new resources";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(OTHER_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
