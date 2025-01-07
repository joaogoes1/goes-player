package com.goesplayer.data.repository

import com.goesplayer.data.datasource.LocalDataSource
import com.goesplayer.data.datasource.MediaDataSource
import com.goesplayer.data.model.Music
import javax.inject.Inject

class MusicRepository @Inject constructor(
    private val mediaDataSource: MediaDataSource,
    private val localDataSource: LocalDataSource,
) {
    suspend fun loadSongs(): List<Music> = mediaDataSource
        .loadSongs()
        .apply {
            localDataSource.saveMusics(this)
        }
}