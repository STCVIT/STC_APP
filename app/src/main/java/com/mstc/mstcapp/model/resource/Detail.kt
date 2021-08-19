package com.mstc.mstcapp.model.resource

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "DETAILS")
class Detail(
    @SerializedName("description")
    var description: String,
    @SerializedName("domain")
    val domain: String,

    @SerializedName("expectation")
    val expectation: String,

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