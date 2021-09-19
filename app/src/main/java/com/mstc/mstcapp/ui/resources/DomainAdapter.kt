package com.mstc.mstcapp.ui.resources

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.mstc.mstcapp.model.Domain

class DomainAdapter() :
    ListAdapter<Domain, DomainViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomainViewHolder =
        DomainViewHolder.create(parent)

    override fun onBindViewHolder(holder: DomainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Domain>() {
        override fun areItemsTheSame(oldItem: Domain, newItem: Domain): Boolean =
            oldItem.domain == newItem.domain

        override fun areContentsTheSame(oldItem: Domain, newItem: Domain): Boolean =
            oldItem.domain == newItem.domain

    }

}