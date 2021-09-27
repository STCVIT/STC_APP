package com.mstc.mstcapp.ui.explore.event

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mstc.mstcapp.data.ResourceRepository
import com.mstc.mstcapp.data.STCDatabase
import com.mstc.mstcapp.model.Result
import com.mstc.mstcapp.model.explore.Event
import kotlinx.coroutines.launch

private const val TAG = "EventViewModel"

class EventViewModel(application: Application) : AndroidViewModel(application) {
    val context: Context by lazy { application.applicationContext }
    private val repository =
        ResourceRepository(context, STCDatabase.getInstance(context))

    fun getEvents(): LiveData<Result<List<Event>>> {
        return repository.getEvents()
    }

    fun refreshEvents() {
        viewModelScope.launch { repository.refreshEvents() }
    }
}