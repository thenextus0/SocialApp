package com.thenextus.socialapp.classes.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thenextus.socialapp.classes.database.entities.ApiUser
import com.thenextus.socialapp.classes.database.AppDatabaseRepository
import com.thenextus.socialapp.classes.database.entities.Friend
import kotlinx.coroutines.launch

class ApiUserViewModel(private val repository: AppDatabaseRepository): ViewModel() {

    private val _allApiUsersByID = MutableLiveData<List<ApiUser>>(ArrayList())
    val allApiUsersByID: LiveData<List<ApiUser>> get() = _allApiUsersByID

    fun getSpesificApiUser(apiUserID: String) = repository.getSpecificApiUser(apiUserID)

    fun loadUsersByIdList(friends: List<Friend>) {
        viewModelScope.launch {
            val userIDList = ArrayList<String>()
            for (i in 0 until friends.size) {
                userIDList.add(friends[i].apiUserID)
            }
            _allApiUsersByID.value = repository.getUsersByIdList(userIDList)
        }
    }


    fun insertApiUser(apiUser: ApiUser) = viewModelScope.launch {
        repository.insertApiUser(apiUser)
    }

    fun deleteApiUser(userID: String) = viewModelScope.launch {
        //repository.deleteApiUser(userID)
    }


}