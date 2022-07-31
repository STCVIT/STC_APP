package `in`.stcvit.stcapp.ui.resources.roadmap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import `in`.stcvit.stcapp.data.ResourceRepository
import `in`.stcvit.stcapp.data.STCDatabase
import `in`.stcvit.stcapp.model.resource.Roadmap
import kotlinx.coroutines.launch
import `in`.stcvit.stcapp.model.Result

class RoadmapViewModel(application: Application) : AndroidViewModel(application) {
    private val context by lazy { application.applicationContext }
    private var repository: ResourceRepository =
        ResourceRepository(context, STCDatabase.getInstance(context))

    fun getRoadmap(domain: String): LiveData<Result<Roadmap>> {
        return repository.getDomainRoadmap(domain)
    }

    fun refreshRoadmap(domain: String) {
        viewModelScope.launch {
            repository.getDomainRoadmap(domain)
        }
    }

}