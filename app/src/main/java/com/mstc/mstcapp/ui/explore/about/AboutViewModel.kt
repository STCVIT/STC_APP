package com.mstc.mstcapp.ui.explore.about

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mstc.mstcapp.data.ResourceRepository
import com.mstc.mstcapp.data.STCDatabase
import com.mstc.mstcapp.model.Result
import com.mstc.mstcapp.model.explore.BoardMember
import kotlinx.coroutines.launch

class AboutViewModel(application: Application) : AndroidViewModel(application) {

    val context: Context by lazy { application.applicationContext }

    private val repository =
        ResourceRepository(context, STCDatabase.getInstance(context))

    fun getBoard(): LiveData<Result<List<BoardMember>>> {
        return repository.getBoardMembers()
    }

    fun refreshBoard() {
        viewModelScope.launch { repository.refreshBoard() }
    }
}