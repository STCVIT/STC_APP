package com.mstc.mstcapp.ui.resources.resource

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mstc.mstcapp.R
import com.mstc.mstcapp.databinding.ItemResourceBinding
import com.mstc.mstcapp.model.resource.Resource
import com.mstc.mstcapp.util.Constants
import com.mstc.mstcapp.util.Functions.Companion.openURL

class ResourceViewHolder(
    private val binding: ItemResourceBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(resource: Resource) {
        binding.apply {
            title.text = resource.title
            description.text = resource.description
            share.setOnClickListener { shareResource(resource.title, resource.link) }
            root.setOnClickListener { openURL(root.context, resource.link) }
        }
    }


    private fun shareResource(title: String, link: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT, """
     $link

     Here is an article for you! This is an article on $title.
     To keep receiving amazing articles like these, check out the STC app here 
     
     ${Constants.PLAY_STORE_URL}
     """.trimIndent()
        )
        binding.root.context.startActivity(Intent.createChooser(intent, "Share Using"))
    }

    companion object {
        fun create(parent: ViewGroup): ResourceViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_resource, parent, false)
            val binding = ItemResourceBinding.bind(view)
            return ResourceViewHolder(binding)
        }
    }
}
