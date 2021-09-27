package com.mstc.mstcapp.ui.explore.about

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.mstc.mstcapp.model.explore.BoardMember

class BoardMemberAdapter : ListAdapter<BoardMember, BoardMemberViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardMemberViewHolder =
        BoardMemberViewHolder.create(parent)

    override fun onBindViewHolder(holder: BoardMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<BoardMember>() {
        override fun areItemsTheSame(oldItem: BoardMember, newItem: BoardMember): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BoardMember, newItem: BoardMember): Boolean =
            oldItem.toString() == newItem.toString()
    }
}