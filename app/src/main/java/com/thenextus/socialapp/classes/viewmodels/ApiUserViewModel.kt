package com.thenextus.socialapp.classes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thenextus.socialapp.classes.database.entities.ApiUser
import com.thenextus.socialapp.classes.database.AppDatabaseRepository
import com.thenextus.socialapp.classes.database.entities.User
import kotlinx.coroutines.launch

class ApiUserViewModel(private val repository: AppDatabaseRepository): ViewModel() {

    var _allApiUsers: LiveData<List<ApiUser>>? = repository.getAllApiUsers()
    val allApiUsers: LiveData<List<ApiUser>>? get() = _allApiUsers

    var _allApiUsersForID = MutableLiveData<ArrayList<ApiUser>>(ArrayList<ApiUser>())

    val allApiUsersForID: LiveData<ArrayList<ApiUser>> get() = _allApiUsersForID


    fun getApiUserForID(id: String): ApiUser? { return repository.getSpesificApiUser(id) }

    fun getApiUserForIDArray(id: String) {
        _allApiUsersForID.value!!.add(repository.getSpesificApiUser(id)!!)
    }

    fun insertApiUser(user: ApiUser) = viewModelScope.launch {
        _allApiUsersForID.value!!.add(user)
        repository.insertApiUser(user)
    }

    fun deleteApiUser(userID: String) = viewModelScope.launch {
        repository.deleteApiUser(userID)
    }


}