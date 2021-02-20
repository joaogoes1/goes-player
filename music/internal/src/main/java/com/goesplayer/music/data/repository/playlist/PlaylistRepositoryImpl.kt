package com.goesplayer.music.data.repository.playlist

import com.goesplayer.music.data.model.Music

class PlaylistRepositoryImpl(
    private val playlistLocalDataSource: com.goesplayer.music.data.datasource.playlist.PlaylistLocalDataSource
) : PlaylistRepository {

    override suspend fun saveCurrentPlaylist(musics: List<Music>) =
        playlistLocalDataSource.saveCurrentPlaylist(musics)

    override suspend fun getCurrentPlaylist(): List<Music> =
        playlistLocalDataSource.getCurrentPlaylist()

    override suspend fun getAllPlaylists(): List<String> =
        playlistLocalDataSource.getAllPlaylists()
}