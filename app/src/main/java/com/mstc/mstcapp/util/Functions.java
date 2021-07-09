package com.mstc.mstcapp.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Functions {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void openURL(View view, Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Snackbar.make(view, "Could not open link!", BaseTransientBottomBar.LENGTH_SHORT)
                    .setAction("Try Again", v -> openURL(view, context, url))
                    .show();
            e.printStackTrace();
        }
    }

    public static long timestampToEpochSeconds(String tzDate) {
        long epoch = 0;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Instant instant = Instant.parse(tzDate);
                epoch = instant.getEpochSecond() * 1000;
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSSSS'Z'", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = sdf.parse(tzDate);
                if (date != null) {
                    epoch = date.getTime();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return epoch;
    }

}

