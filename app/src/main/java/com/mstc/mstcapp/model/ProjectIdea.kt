package com.mstc.mstcapp.model

import com.google.gson.annotations.SerializedName

class ProjectIdea(
    @SerializedName("name")
    val name: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("idea")
    val idea: String,

    @SerializedName("description")
    val description: String,
)