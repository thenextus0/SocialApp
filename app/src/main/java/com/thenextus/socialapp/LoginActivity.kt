package com.thenextus.socialapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.thenextus.socialapp.classes.database.entities.User
import com.thenextus.socialapp.classes.socialapp.ServiceLocator
import com.thenextus.socialapp.classes.viewmodels.EventViewModel
import com.thenextus.socialapp.classes.viewmodels.UserViewModel
import com.thenextus.socialapp.classes.viewmodels.factory.EventViewModelFactory
import com.thenextus.socialapp.classes.viewmodels.factory.UserViewModelFactory
import com.thenextus.socialapp.databinding.ActivityLoginBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.UUID

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var userViewModel: UserViewModel

    private lateinit var eventViewModel: EventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventViewModel = ViewModelProvider(this, EventViewModelFactory()).get(EventViewModel::class.java)
        eventViewModel.initSP(this)

        if (eventViewModel.getSPDataIsLogged(this)) openMainScreen()
        else userViewModel = ViewModelProvider(this@LoginActivity, UserViewModelFactory(ServiceLocator.provideRepository())).get(UserViewModel::class.java)

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmailEditText.text.toString()
            val password = binding.loginPasswordEditText.text.toString()

            if (email.replace("\\s".toRegex(), "") != "") {

                userViewModel.setUserForEmail(email)

                userViewModel.viewModelScope.launch {
                    userViewModel.user.collect { dbData ->
                        dbData?.let { eventViewModel.changeSPUserID(dbData.userID)
                        } ?: run {
                            val newID = UUID.randomUUID().toString()

                            var drawableImage: Drawable = AppCompatResources.getDrawable(this@LoginActivity, R.drawable.profile)!!
                            val bitmapImage = drawableToBitmap(drawableImage)
                            val stringImage = bitmapToString(bitmapImage!!)
                            //Log.d("Fatal", stringImage)

                            val newUser = User(newID, null, null, email, stringImage)
                            userViewModel.insertUser(newUser)

                            eventViewModel.changeSPUserID(newID)
                        }
                        eventViewModel.changeSPUserLogged(true)
                        openMainScreen()
                    }
                }
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