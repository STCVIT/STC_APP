package com.mstc.mstcapp.ui.explore.about

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mstc.mstcapp.model.explore.BoardMember

class BoardMemberAdapter : RecyclerView.Adapter<BoardMemberViewHolder>() {

    var list = listOf<BoardMember>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardMemberViewHolder {
        return BoardMemberViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BoardMemberViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}