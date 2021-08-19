package com.mstc.mstcapp.ui.explore.project

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mstc.mstcapp.data.ResourceRepository
import com.mstc.mstcapp.data.STCDatabase
import com.mstc.mstcapp.model.Result
import com.mstc.mstcapp.model.explore.Project
import kotlinx.coroutines.launch

private const val TAG = "ProjectViewModel"

class ProjectViewModel(application: Application) : AndroidViewModel(application) {
    val context: Context by lazy { application.applicationContext }
    private val repository =
        ResourceRepository(context, STCDatabase.getInstance(context))

    fun getProjects(): LiveData<Result<List<Project>>> {
        return repository.getProjects()
    }

    fun refreshProjects() {
        viewModelScope.launch { repository.refreshProjects() }
    }
}
