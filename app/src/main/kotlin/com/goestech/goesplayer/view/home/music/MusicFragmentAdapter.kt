package com.goestech.goesplayer.view.home.music

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goestech.goesplayer.R
import com.goestech.goesplayer.bussiness.model.MusicModel
import com.goestech.goesplayer.databinding.MusicListItemBinding
import com.goestech.goesplayer.view.home.music.MusicFragmentAdapter.MusicFragmentAdapterViewHolder

class MusicFragmentAdapter(
    private val listener: MusicFragmentListener
) : ListAdapter<MusicModel, MusicFragmentAdapterViewHolder>(MusicDiffUtil) {

    class MusicFragmentAdapterViewHolder(val binding: MusicListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicFragmentAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MusicFragmentAdapterViewHolder(MusicListItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: MusicFragmentAdapterViewHolder, position: Int) {
        val music = currentList[position]
        holder.binding.apply {
            musicListItemName.text = music.displayName ?: music.title ?: music.fileName
            musicListItemArtist.text = music.artist ?: "Unknown"
            Glide
                .with(musicListItemImage)
                .load(Uri.parse(music.albumArtUri))
                .placeholder(R.drawable.album_placeholder)
            musicListItemLayout.setOnClickListener { listener.playMusic(music) }
        }
    }
}