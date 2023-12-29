package com.thenextus.socialapp.retrofit.model

import androidx.lifecycle.LiveData
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

data class UserResponseModel(
    @SerializedName("results")
    val results: List<UserModel>
)
