package com.goesplayer.data.repository

import com.goesplayer.data.datasource.LocalDataSource
import com.goesplayer.data.model.Music
import com.goesplayer.data.model.Playlist
import javax.inject.Inject

class PlaylistRepository @Inject constructor(
    private val dataSource: LocalDataSource,
) {
    suspend fun deletePlaylist(playlistId: Long) =
        dataSource.deletePlaylist(playlistId)

    suspend fun loadPlaylists(): List<Playlist> =
        dataSource.loadPlaylists()

    suspend fun createPlaylist(playlistName: String) =
        dataSource.createPlaylist(playlistName)

    suspend fun addToPlaylist(music: Music, playlist: Playlist) =
        dataSource.addToPlaylist(playlist.id, music.id)

    suspend fun loadPlaylistMusics(playlistId: Long): List<Music> =
        dataSource.loadPlaylistMusics(playlistId)
}
