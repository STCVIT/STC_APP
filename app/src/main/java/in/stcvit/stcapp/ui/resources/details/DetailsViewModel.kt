package `in`.stcvit.stcapp.ui.resources.details

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import `in`.stcvit.stcapp.data.ResourceRepository
import `in`.stcvit.stcapp.data.STCDatabase
import `in`.stcvit.stcapp.model.resource.Detail
import kotlinx.coroutines.launch
import `in`.stcvit.stcapp.model.Result

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    val context: Context by lazy { application.applicationContext }
    private val repository =
        ResourceRepository(context, STCDatabase.getInstance(context))

    fun getDetails(domain: String): LiveData<Result<Detail>> {
        return repository.getDomainDetails(domain)
    }

    fun refreshDetails(domain: String) {
        viewModelScope.launch {
            repository.refreshDetails(domain)
        }
    }

}