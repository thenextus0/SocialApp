package com.thenextus.socialapp.classes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.thenextus.socialapp.R
import com.thenextus.socialapp.classes.database.entities.ApiUser
import com.thenextus.socialapp.classes.database.entities.Friend
import com.thenextus.socialapp.databinding.ProfileRecyclerviewCardBinding
import com.thenextus.socialapp.retrofit.model.UserModel

class ProfileRecyclerViewAdapter(): RecyclerView.Adapter<ProfileRecyclerViewAdapter.CardViewHolder>() {

    private val apiUsers = ArrayList<ApiUser>()

    private var onRemoveClickListener: ProfileRecyclerViewAdapter.OnRemoveClickListener? = null

    fun setFriendData(friends: LiveData<ArrayList<ApiUser>>) {
        apiUsers.addAll(friends.value!!)
    }

    inner class CardViewHolder(val binding: ProfileRecyclerviewCardBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.friendButton.setOnClickListener { onRemoveClickListener?.onRemoveClick(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ProfileRecyclerviewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return apiUsers.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        holder.binding.friendButton.setOnClickListener { onRemoveClickListener?.onRemoveClick(position) }

        holder.binding.name.text = "${apiUsers[position].firstName} ${apiUsers[position].lastName}"

        holder.binding.profilePhoto.load(apiUsers[position].picture!!) {
            crossfade(true)
            placeholder(R.drawable.profile)
            transformations(CircleCropTransformation())
        }

    }

    interface OnRemoveClickListener {
        fun onRemoveClick(position: Int)
    }

    fun setOnRemoveClickListener(listener: ProfileRecyclerViewAdapter.OnRemoveClickListener) {
        this.onRemoveClickListener = listener
    }
}