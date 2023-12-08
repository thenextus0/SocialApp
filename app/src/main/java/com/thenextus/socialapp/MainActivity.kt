package com.thenextus.socialapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.thenextus.socialapp.databinding.ActivityMainBinding
import com.thenextus.socialapp.fragments.MainMenuFragmentDirections
import com.thenextus.socialapp.fragments.ProfileFragmentDirections
import com.thenextus.socialapp.fragments.SettingsFragmentDirections


class MainActivity : AppCompatActivity() {
    private val navHostFragment get() =  supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as NavHostFragment
    private val navController get() = navHostFragment.navController

    private lateinit var binding: ActivityMainBinding

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profileFragment -> {
                    val action = MainMenuFragmentDirections.actionMainMenuFragmentToProfileFragment()
                    navController.navigate(action)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.mainMenuFragment -> {
                    val action = ProfileFragmentDirections.actionProfileFragmentToMainMenuFragment()
                    navController.navigate(action)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setupWithNavController(navController)

        //binding.bottomNavigation.selectedItemId = R.id.mainMenuFragment
        //binding.bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    }

    fun loadFragment() {

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