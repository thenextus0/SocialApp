package com.thenextus.socialapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.KeyEventDispatcher.dispatchKeyEvent
import com.thenextus.socialapp.MainActivity
import com.thenextus.socialapp.R
import com.thenextus.socialapp.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FragmentTest", "Settings created")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).toggleButtonVisibility(false)
        (activity as MainActivity).toogleBottomNavigationVisibility(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}