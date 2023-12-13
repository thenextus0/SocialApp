package com.thenextus.socialapp.classes.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thenextus.socialapp.classes.database.AppDatabaseRepository
import com.thenextus.socialapp.classes.database.entities.Friend
import com.thenextus.socialapp.classes.database.entities.User
import kotlinx.coroutines.launch

class FriendsViewModel(val repository: AppDatabaseRepository): ViewModel() {

    val allFriends = MutableLiveData<List<Friend>>()

    fun getAll(): List<Friend> {
        return repository.getAll()
    }

    fun insertDefault(friend: Friend) : Long {
        return repository.insertDefault(friend)
    }

    fun deleteFriend(friendRowID: String) = viewModelScope.launch {
        repository.deleteUser(friendRowID)
    }

    fun getSpecificFriends(userID: String, apiUserID: String): Friend? {
        return repository.getSpecificFriends(userID, apiUserID)
    }

    fun getAllFriendsByID(userID: String): List<Friend>? {
        return repository.getAllFriendsByID(userID)
    }


}