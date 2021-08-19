package com.mstc.mstcapp.ui.explore.event

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mstc.mstcapp.model.explore.Event

private const val TAG = "EventAdapter"

class EventAdapter : RecyclerView.Adapter<EventViewHolder>() {

    var list = listOf<Event>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder =
        EventViewHolder.create(parent)

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}

