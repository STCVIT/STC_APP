package com.mstc.mstcapp.ui.explore.event

import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Base64
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

class EventViewHolder(
    private val binding: ItemEventBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val MAX_LINES = 3

    fun bind(event: Event) {
        binding.apply {
            title.text = event.title
            view.setOnClickListener { openURL(root.context, event.link) }
            description.post {
                run {
                    description.text = event.description
                    if (description.lineCount > MAX_LINES) {
                        event.expand = true
                        collapseDescription(event)
                        description.setOnClickListener {
                            event.expand = !event.expand
                            if (!event.expand) collapseDescription(event)
                            else expandDescription(event)
                        }
                    }
                }
            }
            status.text = event.status
            status.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    root.context,
                    when (event.status) {
                        "UPCOMING" -> R.color.green
                        "ONGOING" -> R.color.colorSecondaryYellow
                        else -> R.color.red
                    }
                )
            )
            cardView.apply {
                setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        when (layoutPosition % 3) {
                            0 -> R.color.colorTertiaryBlue
                            1 -> R.color.colorTertiaryRed
                            else -> R.color.colorTertiaryYellow
                        }
                    )
                )
            }
        }
        loadImage(event)
    }


    private fun loadImage(event: Event) = runBlocking {
        binding.apply {
            launch(Dispatchers.Default) {
                try {
                    val decodedString: ByteArray =
                        Base64.decode(
                            event.image,
                            Base64.DEFAULT
                        )
                    val picture =
                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    image.setImageBitmap(picture)
                } catch (e: Exception) {
                    image.setImageDrawable(
                        ContextCompat.getDrawable(
                            image.context,
                            R.drawable.ic_error
                        )
                    )
                    e.printStackTrace()
                }
            }
        }
    }

    private fun collapseDescription(event: Event) {
        binding.description.apply {
            val lastCharShown: Int =
                layout.getLineVisibleEnd(MAX_LINES - 1)
            val moreString = "...more"
            val actionDisplayText: String =
                event.description.substring(
                    0,
                    lastCharShown - moreString.length
                ) + moreString
            val truncatedSpannableString = SpannableString(actionDisplayText)
            val startIndex = actionDisplayText.indexOf(moreString)
            truncatedSpannableString.setSpan(
                ForegroundColorSpan(
                    context.getColor(
                        R.color.gray
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
            val suffix = "...View Less"
            val actionDisplayText =
                "${event.description}  $suffix"
            val truncatedSpannableString = SpannableString(actionDisplayText)
            val startIndex = actionDisplayText.indexOf(suffix)
            truncatedSpannableString.setSpan(
                ForegroundColorSpan(
                    context.getColor(R.color.gray)
                ),
                startIndex,
                startIndex + suffix.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            text = truncatedSpannableString
        }
        event.expand = true
    }

    companion object {
        fun create(parent: ViewGroup): EventViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_event, parent, false)
            return EventViewHolder(ItemEventBinding.bind(view))
        }
    }
}