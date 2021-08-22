package com.mstc.mstcapp.ui.home

import android.graphics.BitmapFactory
import android.util.Base64
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

    fun bind(feed: Feed) {
        binding.apply {
            title.text = feed.title
            description.text = feed.description
            description.maxLines = if (feed.expand) 100 else 3

            image.setOnClickListener { openURL(root.context, feed.link) }

            linearLayout.setOnClickListener {
                feed.expand = !feed.expand
                description.maxLines = if (feed.expand) 100 else 3
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
            }
        }
        loadImage(feed)
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
            return FeedViewHolder(ItemFeedBinding.bind(view))
        }
    }

}