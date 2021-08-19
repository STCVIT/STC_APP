package com.mstc.mstcapp.model.resource

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "RESOURCES")
class Resource(
    @SerializedName("title")
    val title: String,
    @SerializedName("link")
    val link: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("domain")
    val domain: String,

    @PrimaryKey
    @SerializedName("_id")
    val id: String,
){
    init {
        description = description
            .trim()
            .replace("  "," ")
    }
}