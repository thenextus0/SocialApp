package com.thenextus.socialapp.classes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.thenextus.socialapp.R
import com.thenextus.socialapp.databinding.MainmenuRecyclerviewCardBinding
import com.thenextus.socialapp.retrofit.model.UserModel

class MainMenuRecyclerViewAdapter(): RecyclerView.Adapter<MainMenuRecyclerViewAdapter.CardViewHolder>() {

    private val userList = mutableListOf<UserModel>()
    private val isFriend = mutableListOf<Boolean>()

    private var onAddClickListener: OnAddClickListener? = null

    fun setUserData(users: List<UserModel>, isFriendList: List<Boolean>) { userList.addAll(users); isFriend.addAll(isFriendList); }

    fun setSingleIsFriendData(position: Int) { isFriend[position] = true }

    inner class CardViewHolder(val binding: MainmenuRecyclerviewCardBinding): RecyclerView.ViewHolder(binding.root) {
        init { binding.friendButton.setOnClickListener { onAddClickListener?.onAddClick(adapterPosition) } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding: MainmenuRecyclerviewCardBinding = MainmenuRecyclerviewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun getItemCount(): Int { return userList.size }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        holder.binding.friendButton.setOnClickListener { onAddClickListener?.onAddClick(position) }

        holder.binding.name.text = "${userList[position].name.first} ${userList[position].name.last}" as String
        holder.binding.email.text = userList[position].email
        holder.binding.profilePhoto.load(userList[position].picture.thumbnail)  {
            crossfade(true)
            placeholder(R.drawable.profile)
            transformations(CircleCropTransformation())
        }

        if (isFriend[position]) {
            holder.binding.friendButton.setImageDrawable(AppCompatResources.getDrawable(holder.binding.friendButton.context, R.drawable.check_circle))
            holder.binding.friendButton.isClickable = false
        } else {
            holder.binding.friendButton.setImageDrawable(AppCompatResources.getDrawable(holder.binding.friendButton.context, R.drawable.add_circle))
            holder.binding.friendButton.isClickable = true
        }

    }

    interface OnAddClickListener {
        fun onAddClick(position: Int)
    }

    fun setOnAddClickListener(listener: OnAddClickListener) {
        this.onAddClickListener = listener
    }

}