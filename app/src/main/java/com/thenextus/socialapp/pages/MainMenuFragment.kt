package com.thenextus.socialapp.pages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.thenextus.socialapp.classes.UserRecyclerViewAdapter
import com.thenextus.socialapp.classes.viewmodel.UsersViewModel
import com.thenextus.socialapp.databinding.FragmentMainMenuBinding
import com.thenextus.socialapp.retrofit.model.UserModel

class MainMenuFragment : Fragment() {

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UsersViewModel by viewModels()

    private lateinit var adapter: UserRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getUsers()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = UserRecyclerViewAdapter()
        binding.recyclerView.adapter = adapter

        userViewModel.userListLiveData.observe(viewLifecycleOwner, Observer { userList ->
            adapter.setUserData(userList)
            adapter.notifyDataSetChanged()
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("RequestTest", "ONDESTROY")
        _binding = null
    }


}