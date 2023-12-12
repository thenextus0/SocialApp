package com.thenextus.socialapp.classes.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thenextus.socialapp.classes.database.AppDatabaseRepository
import com.thenextus.socialapp.classes.viewmodels.UserViewModel

class UserViewModelFactory(private val appDatabaseRepository: AppDatabaseRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(appDatabaseRepository) as T
        }
        throw IllegalArgumentException("Bilinmeyen model.")
    }

}