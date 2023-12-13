package com.thenextus.socialapp.fragments

import android.R.attr.duration
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.thenextus.socialapp.MainActivity
import com.thenextus.socialapp.classes.KeyValues
import com.thenextus.socialapp.classes.adapters.MainMenuRecyclerViewAdapter
import com.thenextus.socialapp.classes.database.entities.ApiUser
import com.thenextus.socialapp.classes.database.entities.Friend
import com.thenextus.socialapp.classes.socialapp.ServiceLocator
import com.thenextus.socialapp.classes.viewmodels.ApiRequestUserViewModel
import com.thenextus.socialapp.classes.viewmodels.ApiUserViewModel
import com.thenextus.socialapp.classes.viewmodels.FriendsViewModel
import com.thenextus.socialapp.classes.viewmodels.factory.ApiUserViewModelFactory
import com.thenextus.socialapp.classes.viewmodels.factory.FriendsViewModelFactory
import com.thenextus.socialapp.databinding.FragmentMainMenuBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID


class MainMenuFragment : Fragment(), MainMenuRecyclerViewAdapter.OnAddClickListener {

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    private val apiRequestUserViewModel: ApiRequestUserViewModel by viewModels()
    private lateinit var apiUserViewModel: ApiUserViewModel
    private lateinit var friendsViewModel: FriendsViewModel

    private lateinit var sharedPreferences: SharedPreferences
    private var currentUserID: String? = null

    private lateinit var adapter: MainMenuRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences(KeyValues.SPUserFile.key, Context.MODE_PRIVATE)
        currentUserID = sharedPreferences.getString(KeyValues.SPUserLoggedID.key, null)

        apiUserViewModel = ViewModelProvider(requireActivity(), ApiUserViewModelFactory(ServiceLocator.provideRepository())).get(ApiUserViewModel::class.java)
        friendsViewModel = ViewModelProvider(requireActivity(), FriendsViewModelFactory(ServiceLocator.provideRepository())).get(FriendsViewModel::class.java)

        apiRequestUserViewModel.getUsers()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MainMenuRecyclerViewAdapter()
        adapter.setOnAddClickListener(this)
        binding.recyclerView.adapter = adapter

        apiRequestUserViewModel.userListLiveData.observe(viewLifecycleOwner, Observer { userList ->
            adapter.setUserData(userList)
            adapter.notifyDataSetChanged()
        })

        (activity as MainActivity).toggleButtonVisibility(false)
        (activity as MainActivity).toogleBottomNavigationVisibility(true)
    }

    override fun onAddClick(position: Int) {
        //add to database
        var acces = apiRequestUserViewModel.userListLiveData.value!![position]

        var apiUser: ApiUser = ApiUser(acces.login.uuid, acces.name.first, acces.name.last, acces.email, acces.picture.medium)
        var friend: Friend = Friend(UUID.randomUUID().toString(), currentUserID!!, apiUser.userID)

        CoroutineScope(Dispatchers.IO).launch {
            var specificUser = apiUserViewModel.getApiUserForID(apiUser.userID)
            if (specificUser == null) apiUserViewModel.insertApiUser(apiUser)

            var specificFriend = friendsViewModel.getSpecificFriends(currentUserID!!, apiUser.userID)
            if (specificFriend == null) friendsViewModel.insertDefault(friend)
            else {
                requireActivity().runOnUiThread { Toast.makeText(requireContext().applicationContext, "Bu kişi zaten arkadaşınız!", Toast.LENGTH_SHORT).show() }
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}