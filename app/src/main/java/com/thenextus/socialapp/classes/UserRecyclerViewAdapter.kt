package com.thenextus.socialapp.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.thenextus.socialapp.databinding.RecyclerviewCardBinding
import com.thenextus.socialapp.retrofit.model.UserModel

class UserRecyclerViewAdapter(): RecyclerView.Adapter<UserRecyclerViewAdapter.CardViewHolder>() {
    class CardViewHolder(val binding: RecyclerviewCardBinding): RecyclerView.ViewHolder(binding.root) {

    }
    private val userList = mutableListOf<UserModel>()

    fun setUserData(users: List<UserModel>) {
        userList.addAll(users)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding: RecyclerviewCardBinding = RecyclerviewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        //holder.binding.profilePhoto.setImageURI()
        holder.binding.name.text = "${userList[position].name.first} ${userList[position].name.last}" as String
        holder.binding.email.text = userList[position].email

        holder.binding.friendButton.setOnClickListener {
            Toast.makeText(holder.binding.root.context, "Clicked ${userList[position].name.first} ${userList[position].name.last}", Toast.LENGTH_LONG).show()
        }

    }
}