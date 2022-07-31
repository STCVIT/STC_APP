package `in`.stcvit.stcapp.data.feed

import `in`.stcvit.stcapp.model.Feed
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FeedDao {
    @Query("SELECT * FROM FEED ORDER BY id DESC")
    fun getFeed(): PagingSource<Int, Feed>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(feeds: List<Feed>)

    @Query("DELETE FROM FEED")
    suspend fun clearAll(): Int

}