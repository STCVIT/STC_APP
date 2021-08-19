package com.mstc.mstcapp.model.explore

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.mstc.mstcapp.util.Functions
import java.util.*

@Entity(tableName = "EVENTS")
class Event(
    @SerializedName("title")
    val title: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("link")
    val link: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("endDate")
    val endDate: String,

    @PrimaryKey
    @SerializedName("_id")
    val id: String,
) {
    init {
        description = description
            .trim()
            .replace("  "," ")
    }

    @Ignore
    val status: String = statusString()

    @Ignore
    var expand: Boolean = false

    private fun statusString(): String {
        val startDate1 = Date(Functions.timestampToEpochSeconds(startDate))
        val endDate1 = Date(Functions.timestampToEpochSeconds(endDate))
        return if (startDate1.after(Date()))
            "UPCOMING"
        else if (startDate1.before(Date()) && endDate1.after(Date()))
            "ONGOING"
        else
            "COMPLETED"
    }
}