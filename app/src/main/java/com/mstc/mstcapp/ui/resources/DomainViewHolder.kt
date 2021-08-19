package com.mstc.mstcapp.ui.resources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.mstc.mstcapp.R
import com.mstc.mstcapp.databinding.ItemDomainBinding
import com.mstc.mstcapp.model.Domain

class DomainViewHolder(
    private val binding: ItemDomainBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Domain) {
        binding.apply {
            domain.text = item.domain
            image.setImageDrawable((ContextCompat.getDrawable(root.context, item.drawable)))
            val drawable = ResourcesCompat.getDrawable(
                root.context.resources,
                R.drawable.bg_resource,
                ContextThemeWrapper(root.context, item.style).theme
            )
            background.background = drawable
        }
    }

    companion object {
        fun create(parent: ViewGroup): DomainViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_domain, parent, false)
            val binding = ItemDomainBinding.bind(view)
            return DomainViewHolder(binding)
        }
    }
}