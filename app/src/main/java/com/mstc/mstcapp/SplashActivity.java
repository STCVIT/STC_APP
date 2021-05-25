package com.mstc.mstcapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.mstc.mstcapp.util.Constants;

public class SplashActivity extends AppCompatActivity {
    private final Context context = this;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = context.getSharedPreferences(Constants.STC_SHARED_PREFERENCES, MODE_PRIVATE);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (sharedPreferences.getBoolean("isFirstLaunch", true))
                startActivity(new Intent(context, WelcomeActivity.class));
            else
                startActivity(new Intent(context, MainActivity.class));
            finish();
        }, 500);

    }
}