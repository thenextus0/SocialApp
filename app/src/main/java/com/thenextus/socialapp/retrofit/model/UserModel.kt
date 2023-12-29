package com.thenextus.socialapp.retrofit.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("picture")
    val picture: UserPictureModel,
    @SerializedName("name")
    val name: UserNameModel,
    @SerializedName("email")
    val email: String,
    @SerializedName("login")
    val login: UserLoginModel
)