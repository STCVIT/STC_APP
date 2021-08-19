package com.mstc.mstcapp.data.feed

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mstc.mstcapp.model.Feed

@Database(
    entities = [
        FeedRemoteKey::class,
        Feed::class],
    version = 1,
    exportSchema = false
)
abstract class FeedDatabase : RoomDatabase() {

    abstract fun feedDao(): FeedDao
    abstract fun feedKeyDao(): FeedKeyDao

    companion object {

        @Volatile
        private var INSTANCE: FeedDatabase? = null

        fun getInstance(context: Context): FeedDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FeedDatabase::class.java, "FeedDatabase.db"
            )
                .build()
    }
}