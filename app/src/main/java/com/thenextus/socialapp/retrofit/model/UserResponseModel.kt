package com.thenextus.socialapp.retrofit.model

import androidx.lifecycle.LiveData
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

data class UserResponseModel(
    @SerializedName("results")
    val results: List<UserModel>
)

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

data class UserPictureModel(
    @SerializedName("large")
    val large: String,
    @SerializedName("medium")
    val medium: String,
    @SerializedName("thumbnail")
    val thumbnail: String
)

data class UserNameModel(
    @SerializedName("title")
    val title: String,
    @SerializedName("first")
    val first: String,
    @SerializedName("last")
    val last: String
)

data class UserLoginModel(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("salt")
    val salt: String,
    @SerializedName("md5")
    val md5: String,
    @SerializedName("sha1")
    val sha1: String,
    @SerializedName("sha256")
    val sha256: String

)




