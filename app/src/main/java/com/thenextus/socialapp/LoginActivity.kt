package com.thenextus.socialapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.thenextus.socialapp.classes.KeyValues
import com.thenextus.socialapp.classes.database.entities.User
import com.thenextus.socialapp.classes.socialapp.ServiceLocator
import com.thenextus.socialapp.classes.viewmodels.UserViewModel
import com.thenextus.socialapp.classes.viewmodels.factory.UserViewModelFactory
import com.thenextus.socialapp.databinding.ActivityLoginBinding
import java.util.UUID

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var userViewModel: UserViewModel

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences(KeyValues.SPUserFile.key, Context.MODE_PRIVATE)
        val isLogged: Boolean = sharedPreferences.getBoolean(KeyValues.SPUserLogged.key, false)

        if (isLogged) openMainScreen()
        else userViewModel = ViewModelProvider(this@LoginActivity, UserViewModelFactory(ServiceLocator.provideRepository())).get(UserViewModel::class.java)

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmailEditText.text.toString()
            val password = binding.loginPasswordEditText.text.toString()

            if (email.replace("\\s".toRegex(), "") != "") {

                userViewModel.setUserForEmail(email)

                userViewModel.user?.observe(this@LoginActivity, Observer { dbData ->
                    dbData?.let {
                        sharedPreferences.edit().putString(KeyValues.SPUserLoggedID.key, dbData.userID).apply()
                    } ?: run {
                        val newID = UUID.randomUUID().toString()
                        val newUser = User(newID, null, null, email, null)
                        userViewModel.insertUser(newUser)
                        sharedPreferences.edit().putString(KeyValues.SPUserLoggedID.key, newID).apply()
                    }
                    sharedPreferences.edit().putBoolean(KeyValues.SPUserLogged.key, true).apply()
                    openMainScreen()

                })
            }
            else Toast.makeText(this, "E-mail alanını boş bırakamazsın.", Toast.LENGTH_SHORT).show()

        }

    }

    fun openMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}