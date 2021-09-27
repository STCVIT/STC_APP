package com.mstc.mstcapp.ui.home

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
import com.mstc.mstcapp.databinding.ItemFeedBinding
import com.mstc.mstcapp.model.Feed
import com.mstc.mstcapp.util.Functions

private const val TAG = "FeedViewHolder"

class FeedViewHolder(
    private val binding: ItemFeedBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(feed: Feed) {
        binding.apply {
            description.text = feed.description
            title.text = feed.title
            makeSpan(feed)
            loadImage(feed)
            image.setOnClickListener { Functions.openLinkWithAnimation(root, feed.link) }
            title.setOnClickListener { Functions.openLinkWithAnimation(root, feed.link) }
            constraintLayout.setOnClickListener { Functions.openLinkWithAnimation(root, feed.link) }
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
        }
    }

    private fun ItemFeedBinding.makeSpan(feed: Feed) {
        var text = feed.description
        description.post {
            description.text = text
            if (description.lineCount > 3 && !feed.expand) {
                val lastCharShown: Int =
                    description.layout.getLineVisibleEnd(1)
                text = feed.description.substring(0, lastCharShown) + "…more"
            } else if (feed.expand) {
                text = feed.description + "…View Less"
            }
            val spannableString = SpannableString(text)
            val extra = object : ClickableSpan() {
                override fun onClick(widget: View) { Functions.openLinkWithAnimation(root, feed.link) }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = ContextCompat.getColor(root.context, R.color.textColorPrimary)
                }
            }
            val more = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    feed.expand = !feed.expand
                    makeSpan(feed)
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

    private fun ItemFeedBinding.loadImage(feed: Feed) {
        try {
            image.post {
                val decodedString: ByteArray =
                    Base64.decode(
                        feed.image,
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
        fun create(parent: ViewGroup): FeedViewHolder {
            val binding =
                ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return FeedViewHolder(binding)
        }
    }

}