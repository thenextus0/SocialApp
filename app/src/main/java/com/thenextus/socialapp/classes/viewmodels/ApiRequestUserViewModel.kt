package com.thenextus.socialapp.classes.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thenextus.socialapp.classes.KeyValues
import com.thenextus.socialapp.retrofit.model.UserModel
import com.thenextus.socialapp.retrofit.model.UserResponseModel
import com.thenextus.socialapp.retrofit.service.RandomUserAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiRequestUserViewModel(): ViewModel() {

    private val BASE_URL = "https://randomuser.me/"

    private val _userListLiveData = MutableLiveData<List<UserModel>>()
    val userListLiveData: LiveData<List<UserModel>> get() = _userListLiveData

    fun getUsers() { if (!_userListLiveData.isInitialized) sendGetDataRequest() }

    private fun sendGetDataRequest() {
        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RandomUserAPI::class.java)
        val call = service.getUsersData()

        call.enqueue(object: Callback<UserResponseModel> {
            override fun onResponse(call: Call<UserResponseModel>, response: Response<UserResponseModel>) {
                if (response.isSuccessful) {
                    response.body()?.let { userResponseModel ->
                        onSuccessfulResponse(userResponseModel)
                    } ?: run { Log.d(KeyValues.LogAPI.key, "Response body is empty") }
                } else (Log.d(KeyValues.LogAPI.key, "Response failed."))
            }

            override fun onFailure(call: Call<UserResponseModel>, t: Throwable) { t.printStackTrace() }
        })

    }

    private fun onSuccessfulResponse(response: UserResponseModel) { _userListLiveData.value = response.results }
}