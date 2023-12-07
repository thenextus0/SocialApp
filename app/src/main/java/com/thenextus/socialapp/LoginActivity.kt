package com.thenextus.socialapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.thenextus.socialapp.classes.KeyValues
import com.thenextus.socialapp.databinding.ActivityLoginBinding
import com.thenextus.socialapp.databinding.ActivityMainBinding
import java.security.Key

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences(KeyValues.SPUserFile.key, Context.MODE_PRIVATE)
        var email: String? = sharedPreferences.getString(KeyValues.SPUserEmail.key, null)

        if (email != null) openMainScreen()
    }

    fun openMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun login(view: View) {
        val email = binding.loginEmailEditText.text.toString()
        val password = binding.loginPasswordEditText.text.toString()

        if (email.replace("\\s".toRegex(), "") != "") {
            sharedPreferences.edit().putString(KeyValues.SPUserEmail.key, email).apply()
            openMainScreen()
        }
        else Toast.makeText(this, "E-mail alanını boş bırakamazsın.", Toast.LENGTH_SHORT).show()

    }
}