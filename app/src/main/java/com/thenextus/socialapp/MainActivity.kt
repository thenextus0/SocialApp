package com.thenextus.socialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.thenextus.socialapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val navHostFragment get() =  supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
    private val navController get() = navHostFragment.navController

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setupWithNavController(navController)
    }

}