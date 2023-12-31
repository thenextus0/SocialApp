package com.thenextus.socialapp.classes.viewmodels

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.thenextus.socialapp.classes.KeyValues
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class EventViewModel: ViewModel() {

    private val _buttonVisibilityChannel = Channel<Boolean>(Channel.BUFFERED)
    val buttonVisibilityFlow = _buttonVisibilityChannel.receiveAsFlow()

    private val _navigationVisibilityChannel = Channel<Boolean>(Channel.BUFFERED)
    val navigationVisibilityFlow = _navigationVisibilityChannel.receiveAsFlow()

    private lateinit var sharedPreferences: SharedPreferences
    var userID: String? = null

    fun setButtonVisibility(showButton: Boolean) {
        _buttonVisibilityChannel.trySend(showButton)
    }

    fun setNavigationVisibility(showNavigation: Boolean) {
        _navigationVisibilityChannel.trySend(showNavigation)
    }

    //sharedPreferences eklenecek.
    //setSharedPreferences.
    fun initSP(activity: Activity) { sharedPreferences = activity.getSharedPreferences(KeyValues.SPUserFile.key, Context.MODE_PRIVATE) }

    fun setUserID(activity: Activity) {
        userID = sharedPreferences.getString(KeyValues.SPUserLoggedID.key, null)
    }

    fun setSPDataToEmpty(activity: Activity) {
        sharedPreferences.edit().putBoolean(KeyValues.SPUserLogged.key, false).apply()
        sharedPreferences.edit().putString(KeyValues.SPUserLoggedID.key, null).apply()
    }

    fun getSPDataIsLogged(activity: Activity): Boolean {
        return sharedPreferences.getBoolean(KeyValues.SPUserLogged.key, false)
    }
    
    fun changeSPUserID(newData: String) {
        sharedPreferences.edit().putString(KeyValues.SPUserLoggedID.key, newData).apply()
    }

    fun changeSPUserLogged(newBoolean: Boolean) {
        sharedPreferences.edit().putBoolean(KeyValues.SPUserLogged.key, newBoolean).apply()
    }


}