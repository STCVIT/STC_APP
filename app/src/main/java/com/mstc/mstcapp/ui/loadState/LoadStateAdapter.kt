package com.mstc.mstcapp.ui.loadState

import android.util.Log
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

private const val TAG = "LoadStateAdapter"

class LoadStateAdapter(
    private val retry: () -> Unit,
) : LoadStateAdapter<LoadStateViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        if (loadState is LoadState.Error && loadState.error.message.toString() == "HTTP 404 Not Found") {
            Log.e(TAG, "onBindViewHolder: End Reached")
            return
        } else {
            Log.e(TAG, "onBindViewHolder: binding load state" )
            holder.bind(loadState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder.create(parent, retry)
    }
}
