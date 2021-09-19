package com.mstc.mstcapp.ui.home

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.mstc.mstcapp.model.Feed

private const val TAG = "FeedAdapter"

class FeedAdapter : PagingDataAdapter<Feed, FeedViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val feed = getItem(position)
        if (feed != null)
            holder.bind(feed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder =
        FeedViewHolder.create(parent)

    companion object DiffCallback : DiffUtil.ItemCallback<Feed>() {
        override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean =
            oldItem.title == newItem.title
    }
}

