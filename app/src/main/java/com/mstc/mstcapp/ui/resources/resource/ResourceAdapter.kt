package com.mstc.mstcapp.ui.resources.resource

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.mstc.mstcapp.model.resource.Resource

class ResourceAdapter : ListAdapter<Resource, ResourceViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Resource>() {
        override fun areItemsTheSame(oldItem: Resource, newItem: Resource): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Resource, newItem: Resource): Boolean =
            oldItem.toString() == newItem.toString()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder =
        ResourceViewHolder.create(parent)

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) =
        holder.bind(getItem(position))

}