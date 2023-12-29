package com.thenextus.socialapp.retrofit.model

import com.google.gson.annotations.SerializedName

data class UserPictureModel(
    @SerializedName("large")
    val large: String,
    @SerializedName("medium")
    val medium: String,
    @SerializedName("thumbnail")
    val thumbnail: String
)