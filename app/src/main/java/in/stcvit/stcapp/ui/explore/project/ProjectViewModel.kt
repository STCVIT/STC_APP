package `in`.stcvit.stcapp.ui.explore.project

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import `in`.stcvit.stcapp.data.ResourceRepository
import `in`.stcvit.stcapp.data.STCDatabase
import `in`.stcvit.stcapp.model.explore.Project
import kotlinx.coroutines.launch
import `in`.stcvit.stcapp.model.Result

class ProjectViewModel(application: Application) : AndroidViewModel(application) {
    val context: Context by lazy { application.applicationContext }
    private val repository =
        ResourceRepository(context, STCDatabase.getInstance(context))

    fun getProjects(): LiveData<Result<List<Project>>> {
        return repository.getProjects()
    }

    fun refreshProjects() {
        viewModelScope.launch { repository.getProjects() }
    }
}
