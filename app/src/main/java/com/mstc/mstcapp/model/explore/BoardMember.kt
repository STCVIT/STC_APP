package com.mstc.mstcapp.model.explore

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "BOARD")
class BoardMember(
    @SerializedName("photo")
    val photo: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("position")
    val position: String,

    @SerializedName("linkedIn")
    val link: String,

    @SerializedName("phrase")
    var phrase: String,

    @PrimaryKey
    @SerializedName("_id")
    val id: String,
){
    init {
        phrase = phrase
            .trim()
            .replace("  "," ")
    }
}