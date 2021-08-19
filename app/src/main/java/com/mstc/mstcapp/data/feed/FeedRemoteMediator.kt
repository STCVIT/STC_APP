package com.mstc.mstcapp.data.feed

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mstc.mstcapp.model.Feed
import com.mstc.mstcapp.util.RetrofitService
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "FeedRemoteMediator"

@OptIn(ExperimentalPagingApi::class)
class FeedRemoteMediator(
    private val service: RetrofitService,
    private val feedDatabase: FeedDatabase,
) : RemoteMediator<Int, Feed>() {
    private val FEED_STARTING_PAGE_INDEX: Int = 1

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
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        try {
            val feeds = service.getFeed(page)
            feeds.let { it ->
                val endOfPaginationReached = it.isEmpty()
                feedDatabase.withTransaction {
                    // clear all tables in the database
                    if (loadType == LoadType.REFRESH) {
                        feedDatabase.feedKeyDao().clearAll()
                        feedDatabase.feedDao().clearAll()
                    }
                    val prevKey = if (page == FEED_STARTING_PAGE_INDEX) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = feeds.map {
                        FeedRemoteKey(feedId = it.id,
                            prevKey = prevKey,
                            nextKey = nextKey)
                    }
                    feedDatabase.feedKeyDao().insertAll(keys)
                    feedDatabase.feedDao().insertAll(it)
                }
                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
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
