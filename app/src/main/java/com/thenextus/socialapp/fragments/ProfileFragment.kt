package com.thenextus.socialapp.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.thenextus.socialapp.LoginActivity
import com.thenextus.socialapp.MainActivity
import com.thenextus.socialapp.R
import com.thenextus.socialapp.classes.KeyValues
import com.thenextus.socialapp.classes.adapters.ProfileRecyclerViewAdapter
import com.thenextus.socialapp.classes.database.entities.ApiUser
import com.thenextus.socialapp.classes.database.entities.Friend
import com.thenextus.socialapp.classes.socialapp.ServiceLocator
import com.thenextus.socialapp.classes.viewmodels.ApiUserViewModel
import com.thenextus.socialapp.classes.viewmodels.FriendsViewModel
import com.thenextus.socialapp.classes.viewmodels.UserViewModel
import com.thenextus.socialapp.classes.viewmodels.factory.ApiUserViewModelFactory
import com.thenextus.socialapp.classes.viewmodels.factory.FriendsViewModelFactory
import com.thenextus.socialapp.classes.viewmodels.factory.UserViewModelFactory
import com.thenextus.socialapp.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class ProfileFragment : Fragment(), ProfileRecyclerViewAdapter.OnRemoveClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProfileRecyclerViewAdapter

    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private var userID: String? = null

    private lateinit var friendsViewModel: FriendsViewModel
    private lateinit var apiUserViewModel: ApiUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendsViewModel = ViewModelProvider(requireActivity(), FriendsViewModelFactory(ServiceLocator.provideRepository())).get(FriendsViewModel::class.java)
        apiUserViewModel = ViewModelProvider(requireActivity(), ApiUserViewModelFactory(ServiceLocator.provideRepository())).get(ApiUserViewModel::class.java)

        userViewModel = ViewModelProvider(requireActivity(), UserViewModelFactory(ServiceLocator.provideRepository())).get(UserViewModel::class.java)
        sharedPreferences = requireActivity().getSharedPreferences(KeyValues.SPUserFile.key, Context.MODE_PRIVATE)
        userID = sharedPreferences.getString(KeyValues.SPUserLoggedID.key, null)

        if (userID.isNullOrBlank()) {
            sharedPreferences.edit().putBoolean(KeyValues.SPUserLogged.key, false).apply()
            sharedPreferences.edit().putString(KeyValues.SPUserLoggedID.key, null).apply()

            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else userViewModel.setUserForID(userID!!)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = ProfileRecyclerViewAdapter()
        adapter.setOnRemoveClickListener(this)
        binding.recyclerView.adapter = adapter

        friendsViewModel.getAllFriendsByID(userID!!).observe(viewLifecycleOwner, Observer { friendList ->
            //println(friendList)
            if (friendList != null) {
                apiUserViewModel.loadUsersByIdList(friendList)
                apiUserViewModel.allApiUsersByID.observe(viewLifecycleOwner, Observer { apiUsers ->
                    if (apiUsers != null) {
                        adapter.setData(apiUsers)
                        adapter.notifyDataSetChanged()
                    }

                })
            }
        })

        userViewModel.user?.observe(requireActivity(), Observer { dbData ->
            binding.emailText.text = dbData.email

            if (dbData != null) {

                if (dbData.firstName != null) binding.usernameText.text = dbData.firstName
                else binding.usernameText.text = "-"

                if (dbData.lastName != null) binding.usernameText.text = "${binding.usernameText.text} ${dbData.lastName}"

                if (dbData.picture != null) {
                    val userBitmapPicture = stringToBitmap(dbData.picture)

                    binding.profilePhoto.load(userBitmapPicture) {
                        crossfade(true)
                        placeholder(R.drawable.profile)
                    }
                }
            }
        })

        (activity as MainActivity).toggleButtonVisibility(true)
        (activity as MainActivity).toogleBottomNavigationVisibility(true)
    }

    override fun onRemoveClick(position: Int, apiUserID: String) {
        friendsViewModel.getSpecificFriend(userID!!, apiUserID).observe(viewLifecycleOwner, Observer {  specificFriend ->
            if (specificFriend != null) friendsViewModel.deleteFriendship(specificFriend.friendRowID)
        })
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