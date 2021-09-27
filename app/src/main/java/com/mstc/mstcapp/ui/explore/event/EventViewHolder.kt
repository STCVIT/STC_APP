package com.mstc.mstcapp.ui.home

import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mstc.mstcapp.R
import com.mstc.mstcapp.databinding.ItemEventBinding
import com.mstc.mstcapp.model.explore.Event
import com.mstc.mstcapp.util.Functions

private const val TAG = "EventViewHolder"

class EventViewHolder(
    private val binding: ItemEventBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(event: Event) {
        binding.apply {
            description.text = event.description
            title.text = event.title
            makeSpan(event)
            loadImage(event)
            image.setOnClickListener { Functions.openLinkWithAnimation(root, event.link) }
            title.setOnClickListener { Functions.openLinkWithAnimation(root, event.link) }
            constraintLayout.setOnClickListener { Functions.openLinkWithAnimation(root, event.link) }
            root.apply {
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
        }
    }

    private fun ItemEventBinding.makeSpan(event: Event) {
        var text = event.description
        description.post {
            description.text = text
            if (description.lineCount > 3 && !event.expand) {
                val lastCharShown: Int =
                    description.layout.getLineVisibleEnd(1)
                text = event.description.substring(0, lastCharShown) + "…more"
            } else if (event.expand) {
                text = event.description + "…View Less"
            }
            val spannableString = SpannableString(text)
            val extra = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Functions.openLinkWithAnimation(root, event.link)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = ContextCompat.getColor(root.context, R.color.textColorPrimary)
                }
            }
            val more = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    event.expand = !event.expand
                    makeSpan(event)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = ContextCompat.getColor(root.context, R.color.gray)
                }
            }
            val allTextStart = 0
            val allTextEnd = text.length
            if (text.contains("…")) {
                spannableString.setSpan(
                    more,
                    text.indexOf("…"),
                    allTextEnd,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                )
                spannableString.setSpan(
                    extra,
                    allTextStart,
                    text.indexOf("…"),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else
                spannableString.setSpan(
                    extra,
                    allTextStart,
                    allTextEnd,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            description.text = spannableString
            description.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun ItemEventBinding.loadImage(event: Event) {
        try {
            image.post {
                val decodedString: ByteArray =
                    Base64.decode(
                        event.image,
                        Base64.DEFAULT
                    )
                val picture =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                image.setImageBitmap(picture)
            }
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

    companion object {
        fun create(parent: ViewGroup): EventViewHolder {
            val binding =
                ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return EventViewHolder(binding)
        }
    }

}