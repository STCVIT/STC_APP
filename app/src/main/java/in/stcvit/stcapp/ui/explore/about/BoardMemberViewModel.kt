package `in`.stcvit.stcapp.ui.explore.about

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import `in`.stcvit.stcapp.data.ResourceRepository
import `in`.stcvit.stcapp.data.STCDatabase
import `in`.stcvit.stcapp.model.explore.BoardMember
import kotlinx.coroutines.launch
import `in`.stcvit.stcapp.model.Result

class BoardMemberViewModel(application: Application) : AndroidViewModel(application) {

    val context: Context by lazy { application.applicationContext }

    private val repository =
        ResourceRepository(context, STCDatabase.getInstance(context))

    fun getBoard(): LiveData<Result<List<BoardMember>>> {
        return repository.getBoardMembers()
    }

    fun refreshBoard() {
        viewModelScope.launch { repository.refreshBoard() }
    }
}