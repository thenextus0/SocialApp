package com.thenextus.socialapp.classes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thenextus.socialapp.classes.viewmodels.repositorys.AppDatabaseRepository
import com.thenextus.socialapp.classes.database.entities.Friend
import kotlinx.coroutines.launch

class FriendsViewModel(val repository: AppDatabaseRepository): ViewModel() {

    fun insertFriend(friend: Friend) = viewModelScope.launch {
        repository.insertFriend(friend)
    }

    fun deleteFriendship(friendRowID: String) = viewModelScope.launch {
        repository.deleteFriendship(friendRowID)
    }

    fun getSpecificFriend(userID: String, apiUserID: String) = repository.getSpecificFriend(userID, apiUserID)

    fun getAllFriendsByID(userID: String): LiveData<List<Friend>> {
        return repository.getAllFriendsByID(userID)
    }

    suspend fun checkFriendship(userID: String, userIDList: List<String>): List<Boolean> {
        return repository.checkFriendshipStatus(userID, userIDList)
    }


}