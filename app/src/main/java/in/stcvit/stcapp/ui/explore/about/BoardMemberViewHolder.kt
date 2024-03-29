package `in`.stcvit.stcapp.ui.explore.about

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import `in`.stcvit.stcapp.R
import `in`.stcvit.stcapp.databinding.ItemBoardMemberBinding
import `in`.stcvit.stcapp.model.explore.BoardMember
import `in`.stcvit.stcapp.util.openLinkWithAnimation

class BoardMemberViewHolder(private val binding: ItemBoardMemberBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(boardMember: BoardMember) {
        binding.apply {
            name.text = boardMember.name
            position.text = boardMember.position
            phrase.text = boardMember.phrase
            root.setOnClickListener {
                openLinkWithAnimation(root, boardMember.link)
            }
            cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    when (layoutPosition % 3) {
                        0 -> R.color.colorTertiaryBlue
                        1 -> R.color.colorTertiaryRed
                        else -> R.color.colorTertiaryYellow
                    }
                )
            )

            Glide.with(root.context)
                .load(boardMember.photo)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.loading_placeholder)
                .fallback(R.drawable.ic_error)
                .into(image)
        }
    }

    companion object {
        fun create(parent: ViewGroup): BoardMemberViewHolder {
            val binding =
                ItemBoardMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return BoardMemberViewHolder(binding)
        }
    }

}
