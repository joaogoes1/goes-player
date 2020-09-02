package com.goestech.goesplayer.data.repository.music

import com.goestech.goesplayer.data.datasource.music.MusicDataSource
import com.goestech.goesplayer.data.datasource.music.MusicStorageDataSource

interface MusicRepository {
    suspend fun loadMusicsFromDeviceStorage()
}

class MusicRepositoryImpl(
    private val musicStorageDataSource: MusicStorageDataSource,
    private val musicLocalDataSource: MusicDataSource
) : MusicRepository {
    override suspend fun loadMusicsFromDeviceStorage() {
        val musicList = musicStorageDataSource.searchAllMusics()
        musicLocalDataSource.saveMusics(musicList)
    }
}
