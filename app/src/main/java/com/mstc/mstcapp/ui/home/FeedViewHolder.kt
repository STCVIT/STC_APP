package com.mstc.mstcapp.ui.home

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
import com.mstc.mstcapp.databinding.ItemFeedBinding
import com.mstc.mstcapp.model.Feed
import com.mstc.mstcapp.util.Functions.Companion.openURL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val TAG = "FeedViewHolder"

class FeedViewHolder(
    private val binding: ItemFeedBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val MAX_LINES = 3

    fun bind(feed: Feed) {
        binding.apply {
            title.text = feed.title

            description.post {
                run {
                    description.text = feed.description
                    if (description.lineCount > MAX_LINES) {
                        feed.expand = true
                        collapseDescription(feed)
                    }
                }
            }
            image.setOnClickListener { openURL(root.context, feed.link) }

            linearLayout.setOnClickListener {
                description.text = feed.description
                if (description.lineCount > MAX_LINES) {
                    if (feed.expand) collapseDescription(feed)
                    else expandDescription(feed)
                } else {
                    Log.i(TAG, "bind: does not exceed limit")
                }
            }
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
//                layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
//                layoutTransition.setDuration(200)
            }
        }
        loadImage(feed)
    }

    private fun collapseDescription(feed: Feed) {
        binding.description.apply {
            val lastCharShown: Int =
                layout.getLineVisibleEnd(MAX_LINES - 1)
            maxLines = MAX_LINES
            val moreString = "Read More"
            val actionDisplayText: String =
                feed.description.substring(
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
        feed.expand = false
    }

    private fun expandDescription(feed: Feed) {
        binding.description.apply {
            val suffix = "Read Less"
            maxLines = 100
            val actionDisplayText =
                "${feed.description}  $suffix"
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
        feed.expand = true
    }

    private fun loadImage(feed: Feed) = runBlocking {
        binding.apply {
            launch(Dispatchers.Default) {
                try {
                    val decodedString: ByteArray =
                        Base64.decode(
                            feed.image,
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

    companion object {
        fun create(parent: ViewGroup): FeedViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_feed, parent, false)
            val binding = ItemFeedBinding.bind(view)
            return FeedViewHolder(binding)
        }
    }

}