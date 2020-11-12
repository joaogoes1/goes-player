package com.goestech.goesplayer.data.repository.playlist

import com.goestech.goesplayer.data.datasource.playlist.PlaylistLocalDataSource
import com.goestech.goesplayer.data.model.Music

class PlaylistRepositoryImpl(
    private val playlistLocalDataSource: PlaylistLocalDataSource
) : PlaylistRepository {

    override suspend fun saveCurrentPlaylist(musics: List<Music>) =
        playlistLocalDataSource.saveCurrentPlaylist(musics)

    override suspend fun getCurrentPlaylist(): List<Music> =
        playlistLocalDataSource.getCurrentPlaylist()
}