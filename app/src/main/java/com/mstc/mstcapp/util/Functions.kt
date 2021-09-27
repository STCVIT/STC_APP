package com.mstc.mstcapp.util

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.mstc.mstcapp.R
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class Functions {
    companion object {
        fun timestampToEpochSeconds(tzDate: String): Long {
            var epoch: Long = 0
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val instant = Instant.parse(tzDate)
                    epoch = instant.epochSecond * 1000
                } else {
                    val sdf =
                        SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSSSS'Z'", Locale.getDefault())
                    sdf.timeZone = TimeZone.getTimeZone("UTC")
                    val date = sdf.parse(tzDate)
                    if (date != null) {
                        epoch = date.time
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return epoch
        }

        fun openURL(context: Context, url: String) {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    context,
                    "Unable to open link! Please try again…",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        fun openLinkWithAnimation(view: View, link: String) {
            view.apply {
                try {
                    foreground = ColorDrawable(
                        ContextCompat.getColor(
                            context,
                            R.color.colorHighlight
                        )
                    )
                    postDelayed({
                        foreground = null
                        invalidate()
                    }, 100)
                    openURL(context, link)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
