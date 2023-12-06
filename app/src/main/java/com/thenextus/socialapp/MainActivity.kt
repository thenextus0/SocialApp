package com.thenextus.socialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.thenextus.socialapp.classes.KeyValues
import com.thenextus.socialapp.classes.viewmodel.UsersViewModel
import com.thenextus.socialapp.databinding.ActivityMainBinding
import com.thenextus.socialapp.pages.LoginFragment
import com.thenextus.socialapp.pages.MainMenuFragmentDirections
import com.thenextus.socialapp.pages.ProfileFragmentDirections
import com.thenextus.socialapp.retrofit.model.UserModel
import com.thenextus.socialapp.retrofit.model.UserResponseModel
import com.thenextus.socialapp.retrofit.service.RandomUserAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val navHostFragment get() =  supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment

    private val navController get() = navHostFragment.navController

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trans = supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, LoginFragment())
            addToBackStack(null)
        }
        trans.commit()


        //binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun changePage(test: NavDirections) { Navigation.findNavController(binding.fragmentContainer).navigate(test) }

}