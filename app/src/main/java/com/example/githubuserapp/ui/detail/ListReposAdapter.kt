package com.example.githubuserapp.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserapp.data.response.RepositoryResponseItem
import com.example.githubuserapp.databinding.ItemReposListBinding

class ListReposAdapter(private val listRepos: ArrayList<RepositoryResponseItem>): RecyclerView.Adapter<ListReposAdapter.ListViewHolder>() {

    class ListViewHolder(var binding: ItemReposListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemReposListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.txtName.text = listRepos[position].name
        holder.binding.txtDescription.text = listRepos[position].description
    }

    override fun getItemCount(): Int = listRepos.size
}