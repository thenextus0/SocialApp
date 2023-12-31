package com.thenextus.socialapp.classes.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thenextus.socialapp.classes.viewmodels.EventViewModel

class EventViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            return EventViewModel() as T
        }
        throw IllegalArgumentException("Bilinmeyen model.")
    }
}