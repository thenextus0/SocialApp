package com.thenextus.socialapp.classes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thenextus.socialapp.classes.database.SocialAppRepository
import com.thenextus.socialapp.classes.database.User
import kotlinx.coroutines.launch

class SocialAppViewModel(private val repository: SocialAppRepository): ViewModel() {

    var allUsers: LiveData<List<User>>? = repository.getAllUsers()

    fun getSpesificUser(userID: String) = repository.getSpesificUser(userID)

    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }

    fun deleteUser(userID: String) = viewModelScope.launch {
        repository.deleteUser(userID)
    }

}