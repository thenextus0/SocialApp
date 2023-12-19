package com.thenextus.socialapp.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.thenextus.socialapp.LoginActivity
import com.thenextus.socialapp.MainActivity
import com.thenextus.socialapp.R
import com.thenextus.socialapp.classes.KeyValues
import com.thenextus.socialapp.classes.database.entities.User
import com.thenextus.socialapp.classes.socialapp.ServiceLocator
import com.thenextus.socialapp.classes.viewmodels.UserViewModel
import com.thenextus.socialapp.classes.viewmodels.factory.UserViewModelFactory
import com.thenextus.socialapp.databinding.FragmentSettingsBinding
import java.io.ByteArrayOutputStream

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userViewModel: UserViewModel
    private var userID: String? = null

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    var selectedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerLauncher()

        userViewModel = ViewModelProvider(requireActivity(), UserViewModelFactory(ServiceLocator.provideRepository())).get(UserViewModel::class.java)
        sharedPreferences = requireActivity().getSharedPreferences(KeyValues.SPUserFile.key, Context.MODE_PRIVATE)
        userID = sharedPreferences.getString(KeyValues.SPUserLoggedID.key, null)

        if (userID.isNullOrBlank()) {
            sharedPreferences.edit().putBoolean(KeyValues.SPUserLogged.key, false).apply()
            sharedPreferences.edit().putString(KeyValues.SPUserLoggedID.key, null).apply()

            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        else {
            userViewModel.user!!.observe(viewLifecycleOwner, Observer { userData ->
                userViewModel.setUserForID(userID!!)
                //println(userData)
                if (userData != null) {
                    if (userData.firstName != null) binding.firstNameEditText.setText(userData.firstName)
                    if (userData.lastName != null) binding.lastNameEditText.setText(userData.lastName)
                    binding.emailEditText.setText(userData.email)

                    if (userData.picture != null) {
                        val userBitmapPicture = stringToBitmap(userData.picture)
                        //binding.profilePhoto.setImageBitmap(userBitmapPicture)
                        binding.profilePhoto.load(userBitmapPicture) {
                            crossfade(true)
                            placeholder(R.drawable.profile)
                        }
                    }
                }
            })
        }

        binding.profilePhoto.setOnClickListener { profileImage -> changeImage(profileImage) }
        binding.setChangesButton.setOnClickListener { setChangesButton -> setChanges(setChangesButton) }

        (activity as MainActivity).toggleButtonVisibility(false)
        (activity as MainActivity).toogleBottomNavigationVisibility(false)
    }

    private fun changeImage(view: View) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES)) {
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", View.OnClickListener { sendRequestForHiger() /*request permission*/ }).show()
                } else { sendRequestForHiger() /*request permission*/ }
            } else {
                /*acces granted*/
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                launcher.launch(intentToGallery)
            }

        } else {

            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", View.OnClickListener { sendRequest() /*request permission*/ }).show()
                } else { sendRequest() /*request permission*/ }
            } else {
                /*acces granted*/
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                launcher.launch(intentToGallery)
            }

        }

    }

    private fun sendRequest() { permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }
    private fun sendRequestForHiger() { permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES) }

    private fun registerLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val intentFromResult = result.data

                if (intentFromResult != null) {
                    val imageData = intentFromResult.data

                    if (imageData != null) {
                        try {
                            val image = binding.profilePhoto
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(requireActivity().contentResolver, imageData) //changed
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                image.setImageBitmap(selectedBitmap)
                            }
                            else {
                                selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageData) //changed
                                image.setImageBitmap(selectedBitmap)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                launcher.launch(intentToGallery)
            } else Toast.makeText(requireActivity(), "Permission needed!", Toast.LENGTH_SHORT).show()

        }
    }

    fun setChanges(view: View) {
        //binding.editTextText.text.toString().replace("\\s".toRegex(), "")

        userViewModel.user!!.observe(viewLifecycleOwner, Observer { userData ->
            userViewModel.setUserForID(userID!!)
            if (userData != null) {
                //println(userData)
                var fNameChange: String?
                var lNameChange: String?
                var emailChange: String = ""
                var pictureChange: String?
                var uniqueEmail = true
                var isChanged = false

                val firstName = binding.firstNameEditText.text.toString()
                if (firstName.replace("\\s".toRegex(), "") != "" && userData.firstName != firstName) { isChanged = true; fNameChange = firstName }
                else fNameChange = userData.firstName

                val lastName = binding.lastNameEditText.text.toString()
                if (lastName.replace("\\s".toRegex(), "") != "" && userData.lastName != lastName) { isChanged = true; lNameChange = lastName }
                else lNameChange = userData.lastName

                val email = binding.emailEditText.text.toString(); userViewModel.controlEmail(email)
                if (email.replace("\\s".toRegex(), "") != "" && userData.email != email) {

                    userViewModel.controlEmail(email)!!.observe(viewLifecycleOwner, Observer { emailUser ->
                        isChanged = true
                        if (emailUser == null) { emailChange = email }
                        else uniqueEmail = false
                    })



                }
                else emailChange = userData.email

                val bitmapImage = drawableToBitmap(binding.profilePhoto.drawable) /*binding.profilePhoto.drawable.toBitmap()*/
                val stringImage = bitmapToString(bitmapImage!!)

                val bitmapDefault = drawableToBitmap(AppCompatResources.getDrawable(requireActivity(), R.drawable.profile)!!)
                val stringDefault = bitmapToString(bitmapDefault!!)

                if (stringImage != stringDefault) { //profil resmi default picture değil.
                    if (stringImage != userData.picture) { isChanged = true; pictureChange = stringImage } /*user kaydettiği bir önceki görseli kullanmıyor.*/
                    else pictureChange = userData.picture
                } else pictureChange = stringDefault /*kullanıcı default fotoğrafı kullanıyor.*/




                if (isChanged) {
                    if (uniqueEmail) {
                        var changedUser = User(userData.userID, fNameChange, lNameChange, emailChange, pictureChange)
                        userViewModel.updateUserInfo(changedUser)

                        Toast.makeText(requireContext(), "Değişiklikler yükleniyor..", Toast.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(), "Değişiklikler yüklendi! Profiline dönebilirsin.", Toast.LENGTH_SHORT).show()
                    } else Toast.makeText(requireContext(), "Bu email adresi kullanılıyor. Değişiklikleri yapmak için lütfen başka bir e-mail adresi giriniz.", Toast.LENGTH_SHORT).show()
                } else Toast.makeText(requireContext(), "Hiçbir değişiklik yapmadınız.", Toast.LENGTH_SHORT).show()


            }
        })


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

    fun stringToBitmap(stringImage: String?): Bitmap? {
        val decodedByte: ByteArray = Base64.decode(stringImage, 0)
        return BitmapFactory
            .decodeByteArray(decodedByte, 0, decodedByte.size)
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}