package com.goestech.goesplayer.data.datasource.playlist

import com.goestech.goesplayer.data.entity.Music

interface PlaylistLocalDataSource {
    suspend fun saveCurrentPlaylist(musicList: List<Music>)
    suspend fun getCurrentPlaylist(): List<Music>
}