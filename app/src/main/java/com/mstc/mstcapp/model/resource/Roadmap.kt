package com.mstc.mstcapp.model.resource

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ROADMAPS")
class Roadmap(
    @SerializedName("domain")
    val domain: String,
    @SerializedName("image")
    val image: String,

    @PrimaryKey
    @SerializedName("_id")
    val id: String,
)