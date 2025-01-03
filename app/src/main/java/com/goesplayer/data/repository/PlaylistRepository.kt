package com.goesplayer.data.repository

import com.goesplayer.data.datasource.LocalDataSource
import com.goesplayer.data.model.Music
import com.goesplayer.data.model.Playlist
import javax.inject.Inject

class PlaylistRepository @Inject constructor(
    private val dataSource: LocalDataSource,
) {
    fun deletePlaylist(playlistId: Long): Boolean =
        dataSource.deletePlaylist(playlistId)

    fun loadPlaylists(): List<Playlist> =
        dataSource.loadPlaylists()

    fun createPlaylist(playlistName: String): Boolean =
        dataSource.createPlaylist(playlistName)

    fun addToPlaylist(music: Music, playlist: Playlist) =
        dataSource.addToPlaylist(music.id, playlist.id)
}