package com.thenextus.socialapp

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.thenextus.socialapp.classes.viewmodels.EventViewModel
import com.thenextus.socialapp.classes.viewmodels.factory.EventViewModelFactory
import com.thenextus.socialapp.databinding.ActivityMainBinding
import com.thenextus.socialapp.fragments.ProfileFragmentDirections
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val navHostFragment get() =  supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as NavHostFragment
    private val navController get() = navHostFragment.navController

    private lateinit var binding: ActivityMainBinding

    private lateinit var eventViewModel: EventViewModel

    //private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setupWithNavController(navController)

        eventViewModel = ViewModelProvider(this, EventViewModelFactory()).get(EventViewModel::class.java)

        eventViewModel.viewModelScope.launch { eventViewModel.buttonVisibilityFlow.collect { showButton -> toggleButtonVisibility(showButton) } }
        eventViewModel.viewModelScope.launch { eventViewModel.navigationVisibilityFlow.collect { showNavigation -> toogleBottomNavigationVisibility(showNavigation) } }

        /*sharedPreferences = this.getSharedPreferences(KeyValues.SPUserFile.key, Context.MODE_PRIVATE)
        val isLogged: Boolean = sharedPreferences.getBoolean(KeyValues.SPUserLogged.key, false)
        val id: String? = sharedPreferences.getString(KeyValues.SPUserLoggedID.key, null)

        println(id)
        println(isLogged)*/

    }

    fun toggleButtonVisibility(showButton: Boolean) { binding.actionButton.isVisible = showButton }

    fun toogleBottomNavigationVisibility(showNavigation: Boolean) { binding.bottomNavigation.isVisible = showNavigation }

    fun openSettings(view: View) {
        val action = ProfileFragmentDirections.actionProfileFragmentToSettingsFragment()
        navController.navigate(action)
    }







/*    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_button -> {
                *//*val navController = Navigation.findNavController(this, R.id.fragmentContainer)
                navController.navigate(R.id.settingsFragment)


                //val action = SettingsFrag

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }*/




}