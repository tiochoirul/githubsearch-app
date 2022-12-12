package com.example.githubuserapp.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.data.response.ItemsItem
import com.example.githubuserapp.databinding.ItemUserListBinding

class ListUserDataAdapter(private val listUserData: ArrayList<ItemsItem>): RecyclerView.Adapter<ListUserDataAdapter.ListViewHolder>() {

    class ListViewHolder(var binding: ItemUserListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.txtUserName.text = listUserData[position].url
        holder.binding.txtName.text = listUserData[position].login

        Glide.with(holder.itemView)
            .load(listUserData[position].avatarUrl)
            .into(holder.binding.imgPhoto)
    }

    override fun getItemCount(): Int = listUserData.size
}