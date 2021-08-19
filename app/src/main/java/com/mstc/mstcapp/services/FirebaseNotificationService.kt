package com.mstc.mstcapp.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mstc.mstcapp.MainActivity
import com.mstc.mstcapp.R
import java.text.SimpleDateFormat
import java.util.*

class FirebaseNotificationService : FirebaseMessagingService() {
    override fun onCreate() {
        super.onCreate()
        createEventNotificationChannel()
        createOtherNotificationChannel()
        //FOR WAKE LOCK
        val pm: PowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val isScreenOn: Boolean = pm.isInteractive // check if screen is on
        if (!isScreenOn) {
            val wl: PowerManager.WakeLock = pm.newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "myApp:notificationLock"
            )
            wl.acquire(3000)
        }
        FirebaseMessaging.getInstance().subscribeToTopic("event")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Could not subscribe"
                }
                Log.d(TAG, msg)
            }
        FirebaseMessaging.getInstance().subscribeToTopic("other")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Could not subscribe"
                }
                Log.d(TAG, msg)
            }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //ONLY WHEN APP IS IN FOREGROUND
        Log.i(TAG, "onMessageReceived: " + remoteMessage.data)
        //onMessageReceived: {startDate=14, description=1, title=10}
        Log.i(TAG, "onMessageReceived: " + remoteMessage.from)
        if (remoteMessage.from == ("/topics/event")) {
            val map: Map<String, String> = remoteMessage.data
            val date = Date(map["startDate"])
            val sdf = SimpleDateFormat("dd-MM HH:mm a")
            val title = map["title"].toString() + " on " + sdf.format(date)
            val description = map["description"]
            sendTopicNotification(title, description)
        } else if (remoteMessage.from == ("/topics/other")) {
            val map: Map<String, String> = remoteMessage.data
            val date = Date(map["startDate"])
            val sdf = SimpleDateFormat("dd-MM HH:mm a")
            val title = map["title"].toString() + " on " + sdf.format(date)
            val description = map["description"]
            sendOtherNotification(title, description)
        }
    }

    override fun onDeletedMessages() {}
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token : $token")
    }

    @SuppressLint("InvalidWakeLockTag")
    private fun sendTopicNotification(title: String, messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, EVENT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stc_white)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(this, R.color.log_bg_color))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManagerCompat: NotificationManagerCompat =
            NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(0, notificationBuilder.build())
    }

    @SuppressLint("InvalidWakeLockTag")
    private fun sendOtherNotification(title: String, messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, OTHER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stc_white)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(this, R.color.log_bg_color))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManagerCompat: NotificationManagerCompat =
            NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(0, notificationBuilder.build())
    }

    //ANDROID 8.0 AND ABOVE
    private fun createEventNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Event Notifications"
            val description = "Notify about upcoming events"
            val importance: Int = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(EVENT_CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createOtherNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Other Notifications"
            val description = "Notify about new resources"
            val importance: Int = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(OTHER_CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val TAG = "NotificationService"
        private const val EVENT_CHANNEL_ID = "EventNotifications"
        private const val OTHER_CHANNEL_ID = "OtherNotifications"
    }
}