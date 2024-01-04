package com.thenextus.socialapp.classes.adapters.diffutil

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.thenextus.socialapp.R
import com.thenextus.socialapp.databinding.MainmenuRecyclerviewCardBinding
import com.thenextus.socialapp.retrofit.model.UserModel

class MainMenuRVAdapter(): RecyclerView.Adapter<MainMenuRVAdapter.RowHolder>() {

    private var userList = mutableListOf<UserModel>()
    private var isFriend = mutableListOf<Boolean>()

    private var onAddClickListener: OnAddClickListener? = null

    inner class RowHolder(val binding: MainmenuRecyclerviewCardBinding): RecyclerView.ViewHolder(binding.root) {
        init { binding.friendButton.setOnClickListener { onAddClickListener?.onAddClick(adapterPosition) } }

        fun bind(item: UserModel, itemIsFriennd: Boolean, position: Int) {

            binding.friendButton.setOnClickListener { onAddClickListener?.onAddClick(position) }

            binding.name.text = "${item.name.first} ${item.name.last}"
            binding.email.text = item.email
            binding.profilePhoto.load(item.picture.thumbnail)  {
                crossfade(true)
                placeholder(R.drawable.profile)
                transformations(CircleCropTransformation())
            }

            if (itemIsFriennd) {
                binding.friendButton.setImageDrawable(AppCompatResources.getDrawable(binding.friendButton.context, R.drawable.check_circle))
                binding.friendButton.isClickable = false
            } else {
                binding.friendButton.setImageDrawable(AppCompatResources.getDrawable(binding.friendButton.context, R.drawable.add_circle))
                binding.friendButton.isClickable = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder { return RowHolder(MainmenuRecyclerviewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)) }

    override fun getItemCount(): Int { return userList.size }

    override fun onBindViewHolder(holder: RowHolder, position: Int) { holder.bind(userList[position], isFriend[position], position) }

    interface OnAddClickListener { fun onAddClick(position: Int) }

    fun setOnClickListener(listener: OnAddClickListener) { this.onAddClickListener = listener }

    fun updateData(newUserList: List<UserModel>, newBooleanData: List<Boolean>) {
        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(isFriend, userList, newBooleanData, newUserList))
        userList.clear()
        isFriend.clear()
        userList.addAll(newUserList)
        isFriend.addAll(newBooleanData)
        diffResult.dispatchUpdatesTo(this)
    }

    class MyDiffCallback(private val oldBooleanList: List<Boolean>, private val oldDataList: List<UserModel>, private val newBooleanList: List<Boolean>, private val newDataList: List<UserModel>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldBooleanList.size
        override fun getNewListSize(): Int = newBooleanList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldBooleanList[oldItemPosition] == newBooleanList[newItemPosition] && oldDataList[oldItemPosition] == newDataList[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldBooleanList[oldItemPosition] == newBooleanList[newItemPosition] && oldDataList[oldItemPosition] == newDataList[newItemPosition])
        }
    }

}