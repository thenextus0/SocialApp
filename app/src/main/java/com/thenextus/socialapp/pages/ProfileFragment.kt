package com.thenextus.socialapp.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.thenextus.socialapp.classes.adapters.ProfileRecyclerViewAdapter
import com.thenextus.socialapp.databinding.FragmentProfileBinding
import com.thenextus.socialapp.databinding.ProfileRecyclerviewCardBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProfileRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}