package com.goesplayer.view.home.music

import androidx.recyclerview.widget.DiffUtil
import com.goesplayer.music.data.model.Music

object MusicDiffUtil : DiffUtil.ItemCallback<Music>() {
    override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean =
        oldItem.musicId == newItem.musicId

    override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean =
        oldItem == newItem
}