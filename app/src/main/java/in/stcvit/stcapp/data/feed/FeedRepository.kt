package `in`.stcvit.stcapp.data.feed

import `in`.stcvit.stcapp.model.Feed
import `in`.stcvit.stcapp.util.RetrofitService
import android.util.Log
import androidx.paging.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan

class FeedRepository(
    private val service: RetrofitService,
    private val feedDatabase: FeedDatabase,
) {
    fun getFeeds(): Flow<PagingData<Feed>> {
        val pagingSourceFactory = { feedDatabase.feedDao().getFeed() }
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
            ),
            remoteMediator = FeedRemoteMediator(
                service,
                feedDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 5
    }
}