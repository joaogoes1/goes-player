package com.goestech.goesplayer.view.home.music

import androidx.recyclerview.widget.DiffUtil
import com.goestech.goesplayer.data.entity.Music

object MusicDiffUtil : DiffUtil.ItemCallback<Music>() {
    override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean =
        oldItem.musicId == newItem.musicId

    override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean =
        oldItem == newItem
}