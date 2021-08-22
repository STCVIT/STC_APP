package com.mstc.mstcapp.ui.explore.event

import android.graphics.BitmapFactory
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

class EventViewHolder(private val binding: ItemEventBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val MAX_LINES = 3

    fun bind(event: Event) {
        binding.apply {
            title.text = event.title
            description.text = event.description

            description.maxLines = if (event.expand) 100 else 3

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
                event.expand = !event.expand
                description.maxLines = if (event.expand) 100 else 3

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
            return EventViewHolder(ItemEventBinding.bind(view))
        }
    }
}