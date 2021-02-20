package com.goesplayer.music.data.repository.playlist

import com.goesplayer.music.data.model.Music

interface PlaylistRepository {
    suspend fun saveCurrentPlaylist(musics: List<Music>)
    suspend fun getCurrentPlaylist(): List<Music>
    suspend fun getAllPlaylists(): List<String>
}