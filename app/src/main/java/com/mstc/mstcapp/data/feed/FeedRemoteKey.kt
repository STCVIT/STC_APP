package com.mstc.mstcapp.data.feed

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FEED_REMOTE_KEYS")
data class FeedRemoteKey(
    @PrimaryKey
    val feedId: String,
    val prevKey: Int?,
    val nextKey: Int?,
)