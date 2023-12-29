package com.thenextus.socialapp.retrofit.model

import com.google.gson.annotations.SerializedName

data class UserNameModel(
    @SerializedName("title")
    val title: String,
    @SerializedName("first")
    val first: String,
    @SerializedName("last")
    val last: String
)