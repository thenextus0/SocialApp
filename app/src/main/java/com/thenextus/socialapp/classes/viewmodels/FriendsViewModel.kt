package com.thenextus.socialapp.classes.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thenextus.socialapp.classes.database.AppDatabaseRepository
import com.thenextus.socialapp.classes.database.entities.Friend
import com.thenextus.socialapp.classes.database.entities.User
import kotlinx.coroutines.launch

class FriendsViewModel(private val repository: AppDatabaseRepository): ViewModel() {

    var allFriends: LiveData<List<Friend>>? = repository.getAllFriends()


    /*fun setFriendsByID(userID: String) = viewModelScope.launch {
        //allFriends = repository.getAllFriendsByUserID(userID)
    }*/

    fun insertFriend(friend: Friend) = viewModelScope.launch {
        repository.insertFriend(friend)
    }

    fun deleteFriend(friendRowID: String) = viewModelScope.launch {
        repository.deleteUser(friendRowID)
    }


    /*fun setFriendByID(userID: String, friendID: String) {

    }*/
}