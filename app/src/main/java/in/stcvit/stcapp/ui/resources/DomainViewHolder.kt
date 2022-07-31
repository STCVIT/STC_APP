package `in`.stcvit.stcapp.ui.resources

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import `in`.stcvit.stcapp.R
import `in`.stcvit.stcapp.databinding.ItemDomainBinding
import `in`.stcvit.stcapp.model.Domain

class DomainViewHolder(
    private val binding: ItemDomainBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Domain) {
        binding.apply {
            domain.text = item.domain
            image.setImageDrawable((ContextCompat.getDrawable(root.context, item.drawable)))
            background.background = ResourcesCompat.getDrawable(
                root.context.resources,
                R.drawable.bg_resource,
                ContextThemeWrapper(root.context, item.style).theme
            )
            root.apply {
                setOnClickListener {
                    foreground = ColorDrawable(
                        ContextCompat.getColor(
                            context,
                            R.color.colorHighlight
                        )
                    )
                    postDelayed({
                        foreground = null
                        invalidate()
                    }, 100)
                }
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): DomainViewHolder {
            val binding =
                ItemDomainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DomainViewHolder(binding)
        }
    }
}