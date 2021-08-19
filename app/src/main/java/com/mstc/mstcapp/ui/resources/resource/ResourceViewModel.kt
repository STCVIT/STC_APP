package com.mstc.mstcapp.ui.resources.resource

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mstc.mstcapp.data.ResourceRepository
import com.mstc.mstcapp.data.STCDatabase
import com.mstc.mstcapp.model.Result
import com.mstc.mstcapp.model.resource.Resource
import kotlinx.coroutines.launch

class ResourceViewModel(application: Application) : AndroidViewModel(application) {
    val context: Context by lazy { application.applicationContext }
    val repository =
        ResourceRepository(context, STCDatabase.getInstance(context))

    fun getResources(domain: String): LiveData<Result<List<Resource>>> {
        return repository.getDomainResources(domain)
    }

    fun refreshResources(domain: String) {
        viewModelScope.launch { repository.refreshResources(domain) }
    }
}