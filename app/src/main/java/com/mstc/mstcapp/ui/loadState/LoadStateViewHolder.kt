package com.mstc.mstcapp.ui.loadState

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.mstc.mstcapp.databinding.ItemFooterBinding

private const val TAG = "LoadStateViewHolder"

class LoadStateViewHolder(
    private val binding: ItemFooterBinding,
    retry: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            Log.e(TAG, "bind: ${loadState.error.localizedMessage}")
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
            val binding =
                ItemFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return LoadStateViewHolder(binding, retry)
        }
    }
}
