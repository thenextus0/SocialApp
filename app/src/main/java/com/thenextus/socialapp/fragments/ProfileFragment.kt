package com.thenextus.socialapp.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.thenextus.socialapp.LoginActivity
import com.thenextus.socialapp.R
import com.thenextus.socialapp.classes.adapters.ProfileRecyclerViewAdapter
import com.thenextus.socialapp.classes.socialapp.ServiceLocator
import com.thenextus.socialapp.classes.viewmodels.ApiUserViewModel
import com.thenextus.socialapp.classes.viewmodels.EventViewModel
import com.thenextus.socialapp.classes.viewmodels.FriendsViewModel
import com.thenextus.socialapp.classes.viewmodels.UserViewModel
import com.thenextus.socialapp.classes.viewmodels.factory.ApiUserViewModelFactory
import com.thenextus.socialapp.classes.viewmodels.factory.EventViewModelFactory
import com.thenextus.socialapp.classes.viewmodels.factory.FriendsViewModelFactory
import com.thenextus.socialapp.classes.viewmodels.factory.UserViewModelFactory
import com.thenextus.socialapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(), ProfileRecyclerViewAdapter.OnRemoveClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProfileRecyclerViewAdapter

    private lateinit var userViewModel: UserViewModel

    private lateinit var friendsViewModel: FriendsViewModel
    private lateinit var apiUserViewModel: ApiUserViewModel

    private lateinit var eventViewModel: EventViewModel

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

        eventViewModel = ViewModelProvider(requireActivity(), EventViewModelFactory()).get(EventViewModel::class.java)
        eventViewModel.initSP(requireActivity())
        eventViewModel.setUserID(requireActivity())

        if (eventViewModel.userID.isNullOrBlank()) {
            eventViewModel.setSPDataToEmpty(requireActivity())

            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else userViewModel.setUserForID(eventViewModel.userID!!)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = ProfileRecyclerViewAdapter()
        adapter.setOnRemoveClickListener(this)
        binding.recyclerView.adapter = adapter

        friendsViewModel.getAllFriendsByID(eventViewModel.userID!!).observe(viewLifecycleOwner, Observer { friendList ->
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

        eventViewModel.setButtonVisibility(true)
        eventViewModel.setNavigationVisibility(true)
    }

    override fun onRemoveClick(position: Int, apiUserID: String) {
        friendsViewModel.getSpecificFriend(eventViewModel.userID!!, apiUserID).observe(viewLifecycleOwner, Observer {  specificFriend ->
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
        //_binding = null
    }
}