package com.thenextus.socialapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.thenextus.socialapp.classes.adapters.MainMenuRVAdapter
import com.thenextus.socialapp.classes.database.entities.ApiUser
import com.thenextus.socialapp.classes.database.entities.Friend
import com.thenextus.socialapp.classes.socialapp.ServiceLocator
import com.thenextus.socialapp.classes.viewmodels.ApiRequestUserViewModel
import com.thenextus.socialapp.classes.viewmodels.ApiUserViewModel
import com.thenextus.socialapp.classes.viewmodels.FriendsViewModel
import com.thenextus.socialapp.classes.viewmodels.EventViewModel
import com.thenextus.socialapp.classes.viewmodels.factory.ApiUserViewModelFactory
import com.thenextus.socialapp.classes.viewmodels.factory.EventViewModelFactory
import com.thenextus.socialapp.classes.viewmodels.factory.FriendsViewModelFactory
import com.thenextus.socialapp.databinding.FragmentMainMenuBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.util.UUID


class MainMenuFragment : Fragment(), MainMenuRVAdapter.OnAddClickListener {

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    private val apiRequestUserViewModel: ApiRequestUserViewModel by viewModels()
    private lateinit var apiUserViewModel: ApiUserViewModel
    private lateinit var friendsViewModel: FriendsViewModel

    private lateinit var adapter: MainMenuRVAdapter

    private lateinit var eventViewModel: EventViewModel

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiUserViewModel = ViewModelProvider(this, ApiUserViewModelFactory(ServiceLocator.provideRepository())).get(ApiUserViewModel::class.java)
        friendsViewModel = ViewModelProvider(this, FriendsViewModelFactory(ServiceLocator.provideRepository())).get(FriendsViewModel::class.java)

        eventViewModel = ViewModelProvider(requireActivity(), EventViewModelFactory()).get(EventViewModel::class.java)

        eventViewModel.initSP(requireActivity())
        eventViewModel.setUserID(requireActivity())

        apiRequestUserViewModel.getUsers()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MainMenuRVAdapter()
        adapter.setOnClickListener(this)
        binding.recyclerView.adapter = adapter

        apiRequestUserViewModel.getUsers()

        apiRequestUserViewModel.userListFlow.onEach { userList ->
            val userIDList = arrayListOf<String>()
            for (i in 0 until userList.size) userIDList.add(userList[i].login.uuid)
            lifecycleScope.launch {
                val isFriendList = friendsViewModel.checkFriendship(eventViewModel.userID!!, userIDList)
                adapter.updateData(userList, isFriendList)
            }
        }.launchIn(lifecycleScope)

        eventViewModel.setButtonVisibility(false)
        eventViewModel.setNavigationVisibility(true)
    }

    override fun onAddClick(position: Int) {

        val access = apiRequestUserViewModel.userListFlow.value[position]
        val apiUser = ApiUser(access.login.uuid, access.name.first, access.name.last, access.email, access.picture.medium)
        val friend = Friend(UUID.randomUUID().toString(), eventViewModel.userID!!, apiUser.userID)

        apiUserViewModel.viewModelScope.launch {
            apiUserViewModel.getSpecificApiUser(apiUser.userID).collect { apiUserFromFlow ->
                if (apiUserFromFlow == null) apiUserViewModel.insertApiUser(apiUser)
            }
        }

        friendsViewModel.viewModelScope.launch {
            friendsViewModel.getSpecificFriend(eventViewModel.userID!!, apiUser.userID)
                .take(1)
                .collect { specificFriend ->
                if (specificFriend == null) {
                    friendsViewModel.insertFriend(friend)
                    Toast.makeText(requireContext(), "Arkadaş eklendi!", Toast.LENGTH_SHORT).show()

                    apiRequestUserViewModel.getUsers()
                    apiRequestUserViewModel.userListFlow.onEach { userList ->
                        val userIDList = arrayListOf<String>()
                        for (i in 0 until userList.size) { userIDList.add(userList[i].login.uuid) }

                        lifecycleScope.launch {
                            var isFriendList = friendsViewModel.checkFriendship(eventViewModel.userID!!, userIDList)
                            adapter.updateData(userList, isFriendList)
                        }
                    }.launchIn(lifecycleScope)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}