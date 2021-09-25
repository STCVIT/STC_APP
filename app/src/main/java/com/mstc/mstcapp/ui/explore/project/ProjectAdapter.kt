package com.mstc.mstcapp.ui.explore.project

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.mstc.mstcapp.model.explore.Project

class ProjectAdapter : ListAdapter<Project, ProjectViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Project>() {
        override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean =
            oldItem.toString() == newItem.toString()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder =
        ProjectViewHolder.create(parent)

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) =
        holder.bind(getItem(position))

}