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
    @Ignore
    var status: String

    init {
        description = description
            .replace("  ", " ")
            .trim()
        val startDate1 = Date(Functions.timestampToEpochSeconds(startDate))
        val endDate1 = Date(Functions.timestampToEpochSeconds(endDate))
        status = when {
            startDate1.after(Date()) -> "UPCOMING"
            startDate1.before(Date()) && endDate1.after(Date()) -> "ONGOING"
            else -> "COMPLETED"
        }
    }

    override fun toString(): String {
        return "$title,$description,$link,$startDate,$endDate"
    }

    @Ignore
    var expand: Boolean = false

}