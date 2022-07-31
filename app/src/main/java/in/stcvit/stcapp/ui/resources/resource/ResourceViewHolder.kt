package `in`.stcvit.stcapp.ui.resources.resource

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.stcvit.stcapp.databinding.ItemResourceBinding
import `in`.stcvit.stcapp.model.resource.Resource
import `in`.stcvit.stcapp.util.Constants
import `in`.stcvit.stcapp.util.openLinkWithAnimation

class ResourceViewHolder(
    private val binding: ItemResourceBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(resource: Resource) {
        binding.apply {
            title.text = resource.title
            description.text = resource.description
            share.setOnClickListener { shareResource(resource.title, resource.link) }
            root.setOnClickListener { openLinkWithAnimation(root, resource.link) }
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
            val binding =
                ItemResourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ResourceViewHolder(binding)
        }
    }
}
