package com.mstc.mstcapp.ui.explore.project

import android.graphics.Rect
import android.text.SpannableString
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mstc.mstcapp.R
import com.mstc.mstcapp.databinding.ItemProjectBinding
import com.mstc.mstcapp.model.explore.Project
import com.mstc.mstcapp.util.Functions.Companion.openURL
import com.mstc.mstcapp.util.ImageWrap


private const val TAG = "ProjectViewHolder"

class ProjectViewHolder(
    private val binding: ItemProjectBinding
) : RecyclerView.ViewHolder(binding.root) {
    var finalHeight: Int = 0
    var finalWidth: Int = 0

    fun bind(project: Project) {
        binding.apply {
            title.text = project.title
            details.post {
                run {
                    details.text = project.description
                    if (details.lineCount < 3)
                        viewMore.visibility = View.GONE
                    else
                        viewMore.visibility = View.VISIBLE
                }
            }
            viewMore.setOnClickListener {
                project.expand = !project.expand
                viewMore.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context,
                        if (!project.expand) R.drawable.ic_baseline_keyboard_arrow_down_24
                        else R.drawable.ic_baseline_keyboard_arrow_up_24
                    )
                )
                makeSpan(project)
            }

            Glide.with(root.context)
                .load(project.image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(image)

            image.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    image.post {
                        run {
                            image.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            finalHeight = image.measuredHeight
                            finalWidth = image.measuredWidth
                            makeSpan(project)
                        }
                    }
                }
            })
            image.clipToOutline = true
            root.setOnClickListener { openURL(root.context, project.link) }
            cardView.apply {
                setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        when (layoutPosition % 3) {
                            0 -> R.color.colorTertiaryBlue
                            1 -> R.color.colorTertiaryRed
                            else -> R.color.colorTertiaryYellow
                        }
                    )
                )
            }
        }
    }

    private fun makeSpan(project: Project) {
        binding.apply {
            var text = project.description
            details.text = text
            if (details.lineCount > 3 && !project.expand) {
                val lastCharShown: Int =
                    details.layout.getLineVisibleEnd(1)
                text = project.description.substring(0, lastCharShown) + "..."
            }
            val spannableString = SpannableString(text)
            val allTextStart = 0
            val allTextEnd = text.length - 1

            val lines: Int
            val bounds = Rect()

            details.paint.getTextBounds(text.substring(0, 10), 0, 1, bounds)
            val fontSpacing: Float = details.paint.fontSpacing
            lines = (finalHeight / fontSpacing).toInt()

            val span = ImageWrap(lines - 1, finalWidth)
            spannableString.setSpan(
                span,
                allTextStart,
                allTextEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            details.text = spannableString
        }

    }

    companion object {
        fun create(parent: ViewGroup): ProjectViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_project, parent, false)
            val binding = ItemProjectBinding.bind(view)
            return ProjectViewHolder(binding)
        }
    }
}