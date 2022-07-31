package `in`.stcvit.stcapp.ui.resources.resource

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import `in`.stcvit.stcapp.data.ResourceRepository
import `in`.stcvit.stcapp.data.STCDatabase
import `in`.stcvit.stcapp.model.resource.Resource
import kotlinx.coroutines.launch
import `in`.stcvit.stcapp.model.Result

class ResourceViewModel(application: Application) : AndroidViewModel(application) {
    val context: Context by lazy { application.applicationContext }
    private val repository =
        ResourceRepository(context, STCDatabase.getInstance(context))

    fun getResources(domain: String): LiveData<Result<List<Resource>>> {
        return repository.getDomainResources(domain)
    }

    fun refreshResources(domain: String) {
        viewModelScope.launch { repository.refreshResources(domain) }
    }
}