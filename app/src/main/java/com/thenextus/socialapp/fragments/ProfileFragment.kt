package com.thenextus.socialapp.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

class ProfileFragment : Fragment(), ProfileRecyclerViewAdapter.OnRemoveClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProfileRecyclerViewAdapter

    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var notNullUserID: String

    private lateinit var friendsViewModel: FriendsViewModel
    private lateinit var apiUserViewModel: ApiUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        friendsViewModel = ViewModelProvider(requireActivity(), FriendsViewModelFactory(ServiceLocator.provideRepository())).get(FriendsViewModel::class.java)
        apiUserViewModel = ViewModelProvider(requireActivity(), ApiUserViewModelFactory(ServiceLocator.provideRepository())).get(ApiUserViewModel::class.java)

        userViewModel = ViewModelProvider(requireActivity(), UserViewModelFactory(ServiceLocator.provideRepository())).get(UserViewModel::class.java)
        sharedPreferences = requireActivity().getSharedPreferences(KeyValues.SPUserFile.key, Context.MODE_PRIVATE)
        val userID: String? = sharedPreferences.getString(KeyValues.SPUserLoggedID.key, null)

        if (userID.isNullOrBlank()) {
            sharedPreferences.edit().putBoolean(KeyValues.SPUserLogged.key, false).apply()
            sharedPreferences.edit().putString(KeyValues.SPUserLoggedID.key, "").apply()

            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else {
            userViewModel.setUserForID(userID)
            notNullUserID = userID
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //friends = friendsViewModel.getAllFriendsByID(notNullUserID)!!

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = ProfileRecyclerViewAdapter()
        adapter.setOnRemoveClickListener(this)
        binding.recyclerView.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            friendsViewModel.getAllFriendsByID(notNullUserID)

            var apiFriendData: ArrayList<ApiUser> = arrayListOf()

            for (i in 0 until friendsViewModel.allFriends.value!!.size) { apiFriendData.add(apiUserViewModel.getApiUserForID(friendsViewModel.allFriends.value!![i].apiUserID)!!) }

            println(apiFriendData)
            val list = apiFriendData as List<ApiUser>

            adapter.setFriendData(list)
            adapter.notifyDataSetChanged()
        }

        userViewModel.user?.observe(requireActivity(), Observer { dbData ->
            binding.emailText.text = dbData.email

            if (dbData.firstName != null) binding.usernameText.text = dbData.firstName
            else binding.usernameText.text = "-"

            if (dbData.picture != null) {
                binding.profilePhoto.load(dbData.picture) {
                    crossfade(true)
                    placeholder(R.drawable.profile)
                    transformations(CircleCropTransformation())
                }
            }
        })

        (activity as MainActivity).toggleButtonVisibility(true)
        (activity as MainActivity).toogleBottomNavigationVisibility(true)
    }

    override fun onRemoveClick(position: Int) {
        Toast.makeText(requireContext(), "Deneme", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}