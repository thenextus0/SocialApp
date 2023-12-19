package com.thenextus.socialapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.thenextus.socialapp.classes.KeyValues
import com.thenextus.socialapp.classes.database.entities.User
import com.thenextus.socialapp.classes.socialapp.ServiceLocator
import com.thenextus.socialapp.classes.viewmodels.UserViewModel
import com.thenextus.socialapp.classes.viewmodels.factory.UserViewModelFactory
import com.thenextus.socialapp.databinding.ActivityLoginBinding
import java.io.ByteArrayOutputStream
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

                        var drawableImage: Drawable = AppCompatResources.getDrawable(this@LoginActivity, R.drawable.profile)!!
                        val bitmapImage = drawableToBitmap(drawableImage)
                        val stringImage = bitmapToString(bitmapImage!!)
                        //Log.d("Fatal", stringImage)

                        val newUser = User(newID, null, null, email, stringImage)
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

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        var width = drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 1
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun bitmapToString(bitmapImage: Bitmap): String {
        val byteArray = ByteArrayOutputStream()
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, byteArray)
        val b = byteArray.toByteArray()
        val imageEncoded: String = android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT)
        return imageEncoded
    }

    fun openMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}