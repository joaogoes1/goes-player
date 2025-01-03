package com.goesplayer.data

import com.goesplayer.data.datasource.MediaDataSource
import com.goesplayer.data.model.Music
import javax.inject.Inject

class MusicRepository @Inject constructor(
    private val mediaDataSource: MediaDataSource,
) {
    fun loadSongs(): List<Music> = mediaDataSource.loadSongs()
}