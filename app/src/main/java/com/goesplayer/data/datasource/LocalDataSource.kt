package com.goesplayer.data.datasource

import android.net.Uri
import com.goesplayer.data.AppDatabase
import com.goesplayer.data.entity.MusicEntity
import com.goesplayer.data.entity.PlaylistEntity
import com.goesplayer.data.entity.PlaylistSongCrossRef
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
            .map { it.toModel() }

    suspend fun deletePlaylist(id: Long) =
        database
            .playlistDao()
            .delete(id)
}

fun PlaylistEntity.toModel() =
    Playlist(
        id = playlistId,
        name = name,
    )

fun MusicEntity.toModel() =
    Music(
        id = musicId,
        fileName = fileName,
        title = title,
        artist = artist,
        album = album,
        genre = genre,
        songUri = Uri.parse(songUri),
        albumArtUri = albumArtUri?.let { Uri.parse(it) },
        durationInSeconds = durationInSeconds,
    )
