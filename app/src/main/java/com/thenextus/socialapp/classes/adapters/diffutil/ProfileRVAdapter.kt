package com.thenextus.socialapp.classes.adapters.diffutil

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.thenextus.socialapp.R
import com.thenextus.socialapp.classes.adapters.ProfileRecyclerViewAdapter
import com.thenextus.socialapp.classes.database.entities.ApiUser
import com.thenextus.socialapp.databinding.ProfileRecyclerviewCardBinding
import com.thenextus.socialapp.retrofit.model.UserModel

class ProfileRVAdapter(): RecyclerView.Adapter<ProfileRVAdapter.ColumnHolder>() {

    var apiUsers = mutableListOf<ApiUser>()

    private var onRemoveClickListener: OnRemoveClickListener? = null

    inner class ColumnHolder(val binding: ProfileRecyclerviewCardBinding): RecyclerView.ViewHolder(binding.root) {
        init { binding.friendButton.setOnClickListener { onRemoveClickListener?.onRemoveClick(adapterPosition, apiUsers[adapterPosition].userID) } }

        fun bind(item: ApiUser, position: Int) {

            binding.friendButton.setOnClickListener { onRemoveClickListener?.onRemoveClick(position, item.userID) }
            binding.name.text = "${item.firstName} ${item.lastName}"
            binding.profilePhoto.load(item.picture!!) {
                crossfade(true)
                placeholder(R.drawable.profile)
                transformations(CircleCropTransformation())
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColumnHolder { return ColumnHolder(ProfileRecyclerviewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)) }

    override fun getItemCount(): Int { return apiUsers.size }

    override fun onBindViewHolder(holder: ColumnHolder, position: Int) { holder.bind(apiUsers[position], position) }

    interface OnRemoveClickListener { fun onRemoveClick(position: Int, apiUserID: String) }

    fun setOnRemoveClickListener(listener: OnRemoveClickListener) { this.onRemoveClickListener = listener }

    fun updateData(newApiUsers: List<ApiUser>) {
        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(apiUsers, newApiUsers))
        apiUsers.clear()
        apiUsers.addAll(newApiUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    class MyDiffCallback(private val oldApiUserList: List<ApiUser>, private val newApiUserList: List<ApiUser>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldApiUserList.size
        override fun getNewListSize(): Int = newApiUserList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldApiUserList[oldItemPosition] == newApiUserList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldApiUserList[oldItemPosition] == newApiUserList[newItemPosition]
        }
    }


}