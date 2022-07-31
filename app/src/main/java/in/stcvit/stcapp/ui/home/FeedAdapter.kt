package `in`.stcvit.stcapp.ui.home

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

class FeedAdapter : PagingDataAdapter<UiModel, FeedViewHolder>(UIMODEL_COMPARATOR) {

    companion object {
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {
            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                (oldItem is UiModel.FeedItem && newItem is UiModel.FeedItem &&
                        oldItem.feed.id == newItem.feed.id) ||
                        (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem &&
                                oldItem.description == newItem.description)

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val uiModel = getItem(position)
        if (uiModel != null && uiModel is UiModel.FeedItem)
            holder.bind(uiModel.feed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder =
        FeedViewHolder.create(parent)
}

