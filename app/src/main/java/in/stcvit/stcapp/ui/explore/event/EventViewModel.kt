package `in`.stcvit.stcapp.ui.explore.event

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import `in`.stcvit.stcapp.data.ResourceRepository
import `in`.stcvit.stcapp.data.STCDatabase
import `in`.stcvit.stcapp.model.explore.Event
import kotlinx.coroutines.launch
import `in`.stcvit.stcapp.model.Result

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