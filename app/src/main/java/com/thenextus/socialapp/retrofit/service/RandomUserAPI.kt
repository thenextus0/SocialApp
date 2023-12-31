package com.thenextus.socialapp.retrofit.service

import com.thenextus.socialapp.retrofit.model.UserResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserAPI {

    @GET("api/")
    suspend fun getUsersData(
        @Query("inc") inc: String,
        @Query("results") results: Int,
        @Query("seed") seed: String,
        @Query("noinfo") noinfo: Boolean
    ): UserResponseModel

    /*
    @GET("api/?inc=picture,name,email,login&results=50&seed=SocialApp&noinfo")
    suspend fun getUsersData(): UserResponseModel
    */


    /*
    @GET("api/?inc=picture,name,email,login&results=50&seed=SocialApp&noinfo")
    fun getUsersData(): Call<UserResponseModel>

    */
}