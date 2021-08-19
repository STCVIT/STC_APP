package com.mstc.mstcapp.model.explore

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "PROJECTS")
class Project(

    @SerializedName("title")
    val title: String,

    @SerializedName("link")
    val link: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("image")
    val image: String,

    @PrimaryKey
    @SerializedName("_id")
    val id: String,

){
    init {
        description = description
            .trim()
            .replace("  "," ")
    }
    @Ignore
    var expand:Boolean = false
}
