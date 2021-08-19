package com.mstc.mstcapp.data.feed

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mstc.mstcapp.model.Feed

@Dao
interface FeedDao {
    @Query("SELECT * FROM FEED ORDER BY id DESC")
    fun getFeed(): PagingSource<Int, Feed>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeed(feed: Feed)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(feeds: List<Feed>)

    @Query("DELETE FROM FEED")
    suspend fun clearAll()



}