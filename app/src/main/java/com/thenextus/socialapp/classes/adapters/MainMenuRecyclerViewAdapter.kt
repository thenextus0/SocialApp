package com.thenextus.socialapp.classes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.thenextus.socialapp.R
import com.thenextus.socialapp.databinding.MainmenuRecyclerviewCardBinding
import com.thenextus.socialapp.retrofit.model.UserModel

class MainMenuRecyclerViewAdapter(): RecyclerView.Adapter<MainMenuRecyclerViewAdapter.CardViewHolder>() {

    private val userList = mutableListOf<UserModel>()

    fun setUserData(users: List<UserModel>) {
        userList.addAll(users)
    }

    class CardViewHolder(val binding: MainmenuRecyclerviewCardBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding: MainmenuRecyclerviewCardBinding = MainmenuRecyclerviewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        holder.binding.name.text = "${userList[position].name.first} ${userList[position].name.last}" as String
        holder.binding.email.text = userList[position].email
        holder.binding.profilePhoto.load(userList[position].picture.thumbnail)  {
            crossfade(true)
            placeholder(R.drawable.profile)
            transformations(CircleCropTransformation())
        }

        holder.binding.friendButton.setOnClickListener {
            Toast.makeText(holder.binding.root.context, "Clicked ${userList[position].name.first} ${userList[position].name.last}", Toast.LENGTH_LONG).show()
        }

    }
}