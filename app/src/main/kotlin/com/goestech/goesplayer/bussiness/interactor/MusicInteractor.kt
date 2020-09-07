package com.goestech.goesplayer.bussiness.interactor

import com.goestech.goesplayer.bussiness.model.MusicModel
import com.goestech.goesplayer.data.repository.music.MusicRepository

interface MusicInteractor {
    suspend fun loadMusicsFromDeviceStorage()
    suspend fun getAllMusics(): List<MusicModel>
}

class MusicInteractorImpl(
    private val musicRepository: MusicRepository
) : MusicInteractor {
    override suspend fun loadMusicsFromDeviceStorage() = musicRepository.loadMusicsFromDeviceStorage()
    override suspend fun getAllMusics(): List<MusicModel> {
        TODO("Not yet implemented")
    }
}