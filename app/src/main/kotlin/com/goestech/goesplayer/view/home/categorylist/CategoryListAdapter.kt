package com.goestech.goesplayer.view.home.categorylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goestech.goesplayer.databinding.SimpleListItemBinding

class CategoryListAdapter : ListAdapter<String, CategoryListAdapter.CategoryItemViewHolder>(CategoryItemItemCallback) {

    class CategoryItemViewHolder(val binding: SimpleListItemBinding) : RecyclerView.ViewHolder(binding.root)

    object CategoryItemItemCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CategoryItemViewHolder(SimpleListItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val item = currentList[position]
        holder.binding.simplesListItemTitle.text = item
    }
}