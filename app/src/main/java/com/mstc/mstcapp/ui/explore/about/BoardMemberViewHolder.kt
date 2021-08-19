package com.mstc.mstcapp.ui.explore.about

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mstc.mstcapp.R
import com.mstc.mstcapp.databinding.ItemBoardMemberBinding
import com.mstc.mstcapp.model.explore.BoardMember
import com.mstc.mstcapp.util.Functions.Companion.openURL

class BoardMemberViewHolder(private val binding: ItemBoardMemberBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(boardMember: BoardMember) {
        binding.apply {
            name.text = boardMember.name
            position.text = boardMember.position
            phrase.text = boardMember.phrase
            root.setOnClickListener { openURL(root.context, boardMember.link) }
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
                .into(image)
        }
    }

    companion object {
        fun create(parent: ViewGroup): BoardMemberViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_board_member, parent, false)
            val binding = ItemBoardMemberBinding.bind(view)
            return BoardMemberViewHolder(binding)
        }
    }

}
