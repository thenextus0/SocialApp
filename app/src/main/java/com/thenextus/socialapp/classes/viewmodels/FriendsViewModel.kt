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

    var _allFriends = MutableLiveData<ArrayList<Friend>>(ArrayList<Friend>())

    val allFriends: LiveData<ArrayList<Friend>> get() = _allFriends

    fun getAll(): List<Friend> {
        return repository.getAll()
    }

    fun insertDefault(friend: Friend) : Long {
        _allFriends.value!!.add(friend)
        return repository.insertDefault(friend)
    }

    fun deleteFriend(friendRowID: String, position: Int) = viewModelScope.launch {
        _allFriends.value!!.removeAt(position)
        repository.deleteFriend(friendRowID)
    }

    fun getSpecificFriends(userID: String, apiUserID: String): Friend? {
        return repository.getSpecificFriends(userID, apiUserID)
    }

    fun getAllFriendsByID(userID: String) {
        _allFriends.value!!.addAll(repository.getAllFriendsByID(userID)!!)
    }


}