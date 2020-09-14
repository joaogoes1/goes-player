package com.goestech.goesplayer.data.repository.music

import com.goestech.goesplayer.data.Result
import com.goestech.goesplayer.data.datasource.music.MusicLocalDataSource
import com.goestech.goesplayer.data.datasource.music.MusicStorageDataSource
import com.goestech.goesplayer.data.datasource.music.SearchMusicError
import com.goestech.goesplayer.data.entity.Music

interface MusicRepository {
    suspend fun loadMusicsFromDeviceStorage(): Result<Unit, SearchMusicError>
    suspend fun getAllMusics(): List<Music>
    suspend fun getAllArtists(): List<String>
}

class MusicRepositoryImpl(
    private val musicStorageDataSource: MusicStorageDataSource,
    private val musicLocalDataSource: MusicLocalDataSource
) : MusicRepository {
    override suspend fun loadMusicsFromDeviceStorage(): Result<Unit, SearchMusicError> =
        musicStorageDataSource
            .searchAllMusics()
            .mapSuccess {
                musicLocalDataSource.saveMusics(it)
            }

    override suspend fun getAllMusics(): List<Music> = musicLocalDataSource.getAllMusics()

    override suspend fun getAllArtists(): List<String> = musicLocalDataSource.getAllArtists()
}
