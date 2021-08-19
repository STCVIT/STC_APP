package com.mstc.mstcapp.ui.explore.event

import android.graphics.BitmapFactory
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mstc.mstcapp.R
import com.mstc.mstcapp.databinding.ItemEventBinding
import com.mstc.mstcapp.model.explore.Event
import com.mstc.mstcapp.util.Functions.Companion.openURL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


private const val TAG = "EventViewHolder"

class EventViewHolder(private val binding: ItemEventBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val MAX_LINES = 3

    fun bind(event: Event) {
        binding.apply {
            title.text = event.title
            description.post {
                run {
                    description.text = event.description
                    if (description.lineCount > MAX_LINES) {
                        event.expand = true
                        collapseDescription(event)
                    }
                }
            }

            status.text = event.status
            status.setTextColor(
                ContextCompat.getColor(
                    root.context,
                    when (event.status) {
                        "UPCOMING" -> R.color.colorSecondaryBlue
                        "ONGOING" -> R.color.colorSecondaryYellow
                        else -> R.color.colorSecondaryRed
                    }
                )
            )
            loadImage(event)
            image.setOnClickListener { openURL(root.context, event.link) }
            linearLayout.setOnClickListener {
                    description.text = event.description
                    if (description.lineCount > MAX_LINES) {
                        if (event.expand) collapseDescription(event)
                        else expandDescription(event)
                    } else {
                        Log.i(TAG, "bind: does not exceed limit")
                    }
                }
            cardView.apply {
                setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        when (layoutPosition % 3){
                            0 -> R.color.colorTertiaryBlue
                            1 -> R.color.colorTertiaryRed
                            else -> R.color.colorTertiaryYellow
                        }
                    )
                )
            }
        }

    }

    private fun collapseDescription(event: Event) {
        binding.description.apply {
            val lastCharShown: Int =
                layout.getLineVisibleEnd(MAX_LINES - 1)
            maxLines = MAX_LINES
            val moreString = "Read More"
            val actionDisplayText: String =
                event.description.substring(
                    0,
                    lastCharShown - "  $moreString".length - 3
                ) + "...  $moreString"
            val truncatedSpannableString = SpannableString(actionDisplayText)
            val startIndex = actionDisplayText.indexOf(moreString)
            truncatedSpannableString.setSpan(
                ForegroundColorSpan(
                    context.getColor(
                        R.color.colorPrimary
                    )
                ),
                startIndex,
                startIndex + moreString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            text = truncatedSpannableString
        }
        event.expand = false
    }

    private fun expandDescription(event: Event) {
        binding.description.apply {
            val suffix = "Read Less"
            maxLines = 100
            val actionDisplayText =
                "${event.description}  $suffix"
            val truncatedSpannableString = SpannableString(actionDisplayText)
            val startIndex = actionDisplayText.indexOf(suffix)
            truncatedSpannableString.setSpan(
                ForegroundColorSpan(
                    context.getColor(
                        R.color.colorPrimary
                    )
                ),
                startIndex,
                startIndex + suffix.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            text = truncatedSpannableString
        }
        event.expand = true
    }

    private fun loadImage(event: Event) = runBlocking {
        launch(Dispatchers.Default) {
            binding.image.apply {
                try {
                    val decodedString: ByteArray =
                        Base64.decode(
                            event.image,
                            Base64.DEFAULT
                        )
                    val picture =
                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    setImageBitmap(picture)
                } catch (e: Exception) {
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_error
                        )
                    )
                    e.printStackTrace()
                }
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): EventViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_event, parent, false)
            val binding = ItemEventBinding.bind(view)
            return EventViewHolder(binding)
        }
    }
}