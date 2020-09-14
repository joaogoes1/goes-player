package com.goestech.goesplayer.view.home.artist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goestech.goesplayer.R
import com.goestech.goesplayer.databinding.SimpleListItemBinding

class ArtistFragmentAdapter(
    val listener: ArtistFragmentListener
) : ListAdapter<String, ArtistFragmentAdapter.ArtistViewHolder>(StringDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArtistViewHolder(SimpleListItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = currentList[position]
        holder.binding.apply {
            simplesListItemTitle.text = artist
            simpleListItemLayout.setOnClickListener { listener.openArtist(artist) }
            simpleListItemImage.setImageResource(R.drawable.album_placeholder)
        }
    }

    inner class ArtistViewHolder(val binding: SimpleListItemBinding) : RecyclerView.ViewHolder(binding.root)
}