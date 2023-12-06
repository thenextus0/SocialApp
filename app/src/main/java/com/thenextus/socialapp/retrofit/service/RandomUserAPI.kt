package com.thenextus.socialapp.retrofit.service

import com.thenextus.socialapp.retrofit.model.UserResponseModel
import retrofit2.Call
import retrofit2.http.GET

interface RandomUserAPI {

    @GET("api/?inc=picture,name,email,login&results=50&seed=SocialApp&noinfo")
    fun getUsersData(): Call<UserResponseModel>
}