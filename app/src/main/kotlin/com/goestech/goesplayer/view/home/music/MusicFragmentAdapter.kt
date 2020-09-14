package com.goestech.goesplayer.view.home.music

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goestech.goesplayer.R
import com.goestech.goesplayer.data.entity.Music
import com.goestech.goesplayer.databinding.MusicListItemBinding
import com.goestech.goesplayer.view.home.music.MusicFragmentAdapter.MusicFragmentAdapterViewHolder

class MusicFragmentAdapter(
    private val listener: MusicFragmentListener
) : ListAdapter<Music, MusicFragmentAdapterViewHolder>(MusicDiffUtil) {

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
            musicListItemLayout.setOnClickListener { listener.playMusic(music) }
            loadImage(albumArtUri = music.albumArtUri)
        }
    }

    private fun MusicListItemBinding.loadImage(albumArtUri: String?){
        Glide
            .with(root.context)
            .load(Uri.parse(albumArtUri))
            .placeholder(R.drawable.album_placeholder)
            .error(R.drawable.album_placeholder)
            .into(musicListItemImage)
    }
}