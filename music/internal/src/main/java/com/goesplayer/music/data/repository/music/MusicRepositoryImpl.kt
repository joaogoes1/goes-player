package com.goesplayer.music.data.repository.music

import com.goesplayer.music.data.model.Music
import com.goesplayer.music.data.datasource.music.DeviceStorageDataSource
import com.goesplayer.music.data.datasource.music.MusicLocalDataSource
import com.goesplayer.music.data.datasource.playlist.PlaylistLocalDataSource
import com.goesplayer.commons.data.Result
import com.goesplayer.music.data.SearchMusicError

class MusicRepositoryImpl(
    private val deviceStorageDataSource: DeviceStorageDataSource,
    private val musicLocalDataSource: MusicLocalDataSource,
    private val playlistLocalDataSource: PlaylistLocalDataSource,
) : MusicRepository {
    override suspend fun loadMusicsFromDeviceStorage(): Result<Unit, SearchMusicError> =
        deviceStorageDataSource
            .searchAllMusics()
            .mapSuccess {
                musicLocalDataSource.saveMusics(it)
            }

    override suspend fun getMusic(musicId: Long): Result<Music, SearchMusicError> = musicLocalDataSource.getMusic(musicId)

    override suspend fun getAllMusics(): List<Music> = musicLocalDataSource.getAllMusics()

    override suspend fun getAllArtists(): List<String> = musicLocalDataSource.getAllArtists()

    override suspend fun getAllAlbums(): List<String> = musicLocalDataSource.getAllAlbums()

    override suspend fun getAllFolders(): List<String> = musicLocalDataSource.getAllFolders()

    override suspend fun getAllGenres(): List<String> = musicLocalDataSource.getAllGenres()

    override suspend fun playMusic(selectedMusic: Music, playlist: List<Music>): Result<Unit, SearchMusicError> =
        Result.Success(playlistLocalDataSource.saveCurrentPlaylist(playlist))
}
