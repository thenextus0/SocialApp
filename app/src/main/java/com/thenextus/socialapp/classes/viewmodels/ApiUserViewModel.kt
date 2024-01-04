package com.thenextus.socialapp.classes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thenextus.socialapp.classes.database.entities.ApiUser
import com.thenextus.socialapp.classes.viewmodels.repositorys.AppDatabaseRepository
import com.thenextus.socialapp.classes.database.entities.Friend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ApiUserViewModel(private val repository: AppDatabaseRepository): ViewModel() {

    private val _allApiUsersByIDFlow = MutableStateFlow<List<ApiUser>>(emptyList())
    val allApiUsersByIDFlow: StateFlow<List<ApiUser>> = _allApiUsersByIDFlow

    fun getSpecificApiUser(apiUserID: String) = repository.getSpecificApiUser(apiUserID)

    fun loadUsersByIdList(friends: List<Friend>) {
        viewModelScope.launch {
            val userIDList = ArrayList<String>()
            for (i in 0 until friends.size) userIDList.add(friends[i].apiUserID);
            _allApiUsersByIDFlow.value = repository.getUsersByIdList(userIDList)
        }
    }

    fun insertApiUser(apiUser: ApiUser) = viewModelScope.launch { repository.insertApiUser(apiUser) }

}