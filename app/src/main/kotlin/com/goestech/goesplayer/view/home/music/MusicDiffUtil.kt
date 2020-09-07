package com.goestech.goesplayer.view.home.music

import androidx.recyclerview.widget.DiffUtil
import com.goestech.goesplayer.bussiness.model.MusicModel

object MusicDiffUtil : DiffUtil.ItemCallback<MusicModel>() {
    override fun areItemsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean =
        oldItem == newItem
}