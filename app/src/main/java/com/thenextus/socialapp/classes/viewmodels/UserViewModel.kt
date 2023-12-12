package com.thenextus.socialapp.classes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thenextus.socialapp.classes.database.entities.User
import com.thenextus.socialapp.classes.database.AppDatabaseRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: AppDatabaseRepository): ViewModel() {
    var allUsers: LiveData<List<User>>? = repository.getAllUsers()

    var user: LiveData<User>? = null

    fun setUserForEmail(email: String) { user = repository.getSpesificUserByEmail(email) }

    fun setUserForID(id: String) { user = repository.getSpesificUser(id) }

    fun getSpesificUser(userID: String) = repository.getSpesificUser(userID)

    fun getSpesificUserByEmail(email: String) = repository.getSpesificUserByEmail(email)

    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }

    fun deleteUser(userID: String) = viewModelScope.launch {
        repository.deleteUser(userID)
    }

}