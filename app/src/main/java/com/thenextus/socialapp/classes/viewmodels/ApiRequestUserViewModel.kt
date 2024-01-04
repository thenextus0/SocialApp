package com.thenextus.socialapp.classes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thenextus.socialapp.classes.socialapp.ServiceLocator
import com.thenextus.socialapp.retrofit.model.UserModel
import com.thenextus.socialapp.retrofit.model.UserResponseModel
import com.thenextus.socialapp.retrofit.service.RandomUserAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch

class ApiRequestUserViewModel(): ViewModel() {

    private val _userListFlow = MutableStateFlow<List<UserModel>>(emptyList())
    val userListFlow: StateFlow<List<UserModel>> = _userListFlow

    fun getUsers() { if (_userListFlow.value.isEmpty()) { viewModelScope.launch { sendGetDataRequest() } } }

    private suspend fun sendGetDataRequest() {
        try {
            var retrofit = ServiceLocator.provideRetrofit()
            val service = retrofit.create(RandomUserAPI::class.java)

            val userResponseModel = service.getUsersData("picture,name,email,login", 50, "SocialApp", true)

            onSuccessfulResponse(userResponseModel)
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun onSuccessfulResponse(response: UserResponseModel) {
        viewModelScope.launch {
            _userListFlow.emit(response.results)
        }
    }
}