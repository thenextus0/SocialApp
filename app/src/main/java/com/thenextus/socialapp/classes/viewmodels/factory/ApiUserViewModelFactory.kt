package com.thenextus.socialapp.classes.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thenextus.socialapp.classes.viewmodels.repositorys.AppDatabaseRepository
import com.thenextus.socialapp.classes.viewmodels.ApiUserViewModel

class ApiUserViewModelFactory(private val appDatabaseRepository: AppDatabaseRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApiUserViewModel::class.java)) {
            return ApiUserViewModel(appDatabaseRepository) as T
        }
        throw IllegalArgumentException("Bilinmeyen model.")
    }
}