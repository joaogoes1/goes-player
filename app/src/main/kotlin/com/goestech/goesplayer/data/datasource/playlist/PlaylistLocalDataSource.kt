package com.goestech.goesplayer.data.datasource.playlist

import com.goestech.goesplayer.data.model.Music

interface PlaylistLocalDataSource {
    suspend fun saveCurrentPlaylist(musicList: List<Music>)
    suspend fun getCurrentPlaylist(): List<Music>
    suspend fun getAllPlaylists(): List<String>
}