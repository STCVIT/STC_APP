package com.mstc.mstcapp.ui.resources

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mstc.mstcapp.model.Domain

class DomainAdapter() :
    RecyclerView.Adapter<DomainViewHolder>() {

    var list = listOf<Domain>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomainViewHolder {
        return DomainViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DomainViewHolder, position: Int) {
        holder.bind(item = list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}