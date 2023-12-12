package com.thenextus.socialapp.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.thenextus.socialapp.LoginActivity
import com.thenextus.socialapp.MainActivity
import com.thenextus.socialapp.classes.KeyValues
import com.thenextus.socialapp.classes.adapters.ProfileRecyclerViewAdapter
import com.thenextus.socialapp.classes.socialapp.ServiceLocator
import com.thenextus.socialapp.classes.viewmodels.UserViewModel
import com.thenextus.socialapp.classes.viewmodels.factory.UserViewModelFactory
import com.thenextus.socialapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProfileRecyclerViewAdapter

    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = ProfileRecyclerViewAdapter()
        binding.recyclerView.adapter = adapter

        userViewModel.user?.observe(requireActivity(), Observer { dbData ->
            binding.emailText.text = dbData.email
            binding.usernameText.text = dbData.firstName ?: "-"

            //imageLoad
        })


        (activity as MainActivity).toggleButtonVisibility(true)
        (activity as MainActivity).toogleBottomNavigationVisibility(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}