package `in`.stcvit.stcapp.data.feed

import `in`.stcvit.stcapp.model.Feed
import `in`.stcvit.stcapp.util.RetrofitService
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import java.io.IOException

private const val FEED_STARTING_PAGE_INDEX: Int = 1

@OptIn(ExperimentalPagingApi::class)
class FeedRemoteMediator(
    private val service: RetrofitService,
    private val feedDatabase: FeedDatabase,
) : RemoteMediator<Int, Feed>() {

    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Feed>): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: FEED_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey =
                    remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey =
                    remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        try {
            val response = service.getFeed(page)
            if (response.isSuccessful) {
                response.body()?.let { it ->
                    val endOfPaginationReached = it.isEmpty()
                    feedDatabase.withTransaction {
                        // clear all tables in the database
                        if (loadType == LoadType.REFRESH) {
                            feedDatabase.feedKeyDao().clearAll()
                            feedDatabase.feedDao().clearAll()
                        }
                        val prevKey = if (page == FEED_STARTING_PAGE_INDEX) null else page - 1
                        val nextKey = if (endOfPaginationReached) null else page + 1
                        val keys = it.map { feed ->
                            FeedRemoteKey(
                                feedId = feed.id,
                                prevKey = prevKey,
                                nextKey = nextKey
                            )
                        }
                        feedDatabase.feedKeyDao().insertAll(keys)
                        feedDatabase.feedDao().insertAll(it)
                    }
                    return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }
            } else {
                return MediatorResult.Error(IOException(response.message()))
            }
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
        return MediatorResult.Error(Exception("Unknown Error"))
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Feed>): FeedRemoteKey? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { feed ->
                // Get the remote keys of the last item retrieved
                feedDatabase.feedKeyDao().feedRemoteKeysId(feed.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Feed>): FeedRemoteKey? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { feed ->
                // Get the remote keys of the first items retrieved
                feedDatabase.feedKeyDao().feedRemoteKeysId(feed.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Feed>,
    ): FeedRemoteKey? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { feedId ->
                feedDatabase.feedKeyDao().feedRemoteKeysId(feedId)
            }
        }
    }
}
