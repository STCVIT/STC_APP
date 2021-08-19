package com.mstc.mstcapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mstc.mstcapp.util.Constants.Companion.STC_SHARED_PREFERENCES
import kotlinx.coroutines.*

private const val TAG = "SplashActivity"

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        GlobalScope.launch {
            delay(500)
            val sharedPreferences: SharedPreferences =
                getSharedPreferences(STC_SHARED_PREFERENCES, MODE_PRIVATE)
            startActivity(
                Intent(this@SplashActivity,
                    when {
                        sharedPreferences.getBoolean("isFirstLaunch",
                            true) -> WelcomeActivity::class.java
                        else -> MainActivity::class.java
                    }
                ))
            finish()
        }
    }
}