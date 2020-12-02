package com.goestech.goesplayer.data.repository.playlist

import com.goestech.goesplayer.data.model.Music

interface PlaylistRepository {
    suspend fun saveCurrentPlaylist(musics: List<Music>)
    suspend fun getCurrentPlaylist(): List<Music>
    suspend fun getAllPlaylists(): List<String>
}