package com.mstc.mstcapp

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mstc.mstcapp.util.Constants.Companion.STC_SHARED_PREFERENCES
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lifecycleScope.launch {
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
            delay(500)
            val sharedPreferences: SharedPreferences =
                getSharedPreferences(STC_SHARED_PREFERENCES, MODE_PRIVATE)
            startActivity(
                Intent(
                    this@SplashActivity,
                    when {
                        sharedPreferences.getBoolean(
                            "isFirstLaunch",
                            true
                        ) -> WelcomeActivity::class.java
                        else -> MainActivity::class.java
                    }
                )
            )
            finish()
        }
    }
}