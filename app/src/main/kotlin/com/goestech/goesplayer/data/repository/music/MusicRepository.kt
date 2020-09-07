package com.goestech.goesplayer.data.repository.music

import com.goestech.goesplayer.bussiness.model.MusicModel
import com.goestech.goesplayer.data.datasource.music.MusicLocalDataSource
import com.goestech.goesplayer.data.datasource.music.MusicStorageDataSource

interface MusicRepository {
    suspend fun loadMusicsFromDeviceStorage()
    suspend fun getAllMusics(): List<MusicModel>
}

class MusicRepositoryImpl(
    private val musicStorageDataSource: MusicStorageDataSource,
    private val musicLocalDataSource: MusicLocalDataSource
) : MusicRepository {
    override suspend fun loadMusicsFromDeviceStorage() {
        val musicList = musicStorageDataSource.searchAllMusics()
        musicLocalDataSource.saveMusics(musicList)
    }

    override suspend fun getAllMusics(): List<MusicModel> =
        musicLocalDataSource.getAllMusics()
}
