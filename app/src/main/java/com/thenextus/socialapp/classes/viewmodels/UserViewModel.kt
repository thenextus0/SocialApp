package com.thenextus.socialapp.classes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thenextus.socialapp.classes.database.entities.User
import com.thenextus.socialapp.classes.viewmodels.repositorys.AppDatabaseRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: AppDatabaseRepository): ViewModel() {
    var allUsers: LiveData<List<User>>? = repository.getAllUsers()

    var user: LiveData<User>? = null

    fun setUserForEmail(email: String) { user = repository.getSpesificUserByEmail(email) }

    fun setUserForID(id: String) { user = repository.getSpesificUser(id) }

    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }

    fun updateUserInfo(changedUser: User) = viewModelScope.launch {
        repository.updateUserInfo(changedUser)
    }

    fun controlEmail(email: String): LiveData<User>? {
        return repository.getSpesificUserByEmail(email)
    }
}