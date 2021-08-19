package com.mstc.mstcapp.ui.explore.project

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mstc.mstcapp.model.explore.Project

private const val TAG = "ProjectAdapter"

class ProjectAdapter : RecyclerView.Adapter<ProjectViewHolder>() {
    var list = listOf<Project>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        return ProjectViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}

