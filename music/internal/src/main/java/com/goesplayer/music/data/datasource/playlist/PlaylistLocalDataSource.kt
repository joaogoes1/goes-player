package com.goesplayer.music.data.datasource.playlist

import com.goesplayer.music.data.model.Music

interface PlaylistLocalDataSource {
    suspend fun saveCurrentPlaylist(musicList: List<Music>)
    suspend fun getCurrentPlaylist(): List<Music>
    suspend fun getAllPlaylists(): List<String>
}