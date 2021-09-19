package com.mstc.mstcapp.ui.resources.roadmap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mstc.mstcapp.data.ResourceRepository
import com.mstc.mstcapp.data.STCDatabase
import com.mstc.mstcapp.model.Result
import com.mstc.mstcapp.model.resource.Roadmap
import kotlinx.coroutines.launch

private const val TAG = "RoadmapViewModel"

class RoadmapViewModel(application: Application) : AndroidViewModel(application) {
    private val context by lazy { application.applicationContext }
    private var repository: ResourceRepository =
        ResourceRepository(context, STCDatabase.getInstance(context))

    fun getRoadmap(domain: String): LiveData<Result<Roadmap>> {
        return repository.getDomainRoadmap(domain)
    }

    fun refreshRoadmap(domain: String) {
        viewModelScope.launch {
            repository.refreshRoadmap(domain) }
    }

}