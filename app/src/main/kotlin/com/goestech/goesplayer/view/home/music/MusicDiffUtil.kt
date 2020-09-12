package com.goestech.goesplayer.view.home.music

import androidx.recyclerview.widget.DiffUtil
import com.goestech.goesplayer.data.entity.Music

object MusicDiffUtil : DiffUtil.ItemCallback<Music>() {
    override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean =
        oldItem == newItem
}