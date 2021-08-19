package com.mstc.mstcapp.ui.resources.details

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mstc.mstcapp.data.ResourceRepository
import com.mstc.mstcapp.data.STCDatabase
import com.mstc.mstcapp.model.Result
import com.mstc.mstcapp.model.resource.Detail
import kotlinx.coroutines.launch

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