package com.thenextus.socialapp.classes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thenextus.socialapp.databinding.ProfileRecyclerviewCardBinding
import com.thenextus.socialapp.retrofit.model.UserModel

class ProfileRecyclerViewAdapter(): RecyclerView.Adapter<ProfileRecyclerViewAdapter.CardViewHolder>() {

    private val friendList = mutableListOf<String>("Deneme1dsglbjsdklhnsdlkhbmdsbh","Deneme2sdlngslşdhnsdşlhsdh","Deneme3","Deneme4","Deneme5","Deneme6","Deneme7","Deneme8","Deneme9","Deneme10")

    fun setFriendData(friends: List<String>) {



        friendList.addAll(friends)
    }

    class CardViewHolder(val binding: ProfileRecyclerviewCardBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ProfileRecyclerviewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.binding.name.text = friendList[position]


    }
}