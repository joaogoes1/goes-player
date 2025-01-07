package com.goesplayer.data.datasource

import com.goesplayer.data.AppDatabase
import com.goesplayer.data.entity.PlaylistEntity
import com.goesplayer.data.entity.PlaylistSongCrossRef
import com.goesplayer.data.mapper.toEntity
import com.goesplayer.data.mapper.toModel
import com.goesplayer.data.model.Music
import com.goesplayer.data.model.Playlist
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val database: AppDatabase,
) {
    suspend fun createPlaylist(name: String) {
        database
            .playlistDao()
            .insert(PlaylistEntity(name = name))
    }

    suspend fun addToPlaylist(playlist: Long, music: Long) {
        database
            .playlistDao()
            .insertMusic(PlaylistSongCrossRef(playlist, music))
    }

    suspend fun loadPlaylists(): List<Playlist> =
        database
            .playlistDao()
            .getAll()
            .toModel()

    suspend fun deletePlaylist(id: Long) =
        database
            .playlistDao()
            .delete(id)

    suspend fun loadPlaylistMusics(playlistId: Long) =
        database
            .playlistDao()
            .getPlaylistWithSongs(playlistId)
            .musics
            .toModel()

    suspend fun saveMusics(musics: List<Music>) =
        database
            .musicDao()
            .insertAll(musics.toEntity())
}
