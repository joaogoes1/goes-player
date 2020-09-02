package com.goestech.goesplayer.bussiness.interactor

import com.goestech.goesplayer.data.repository.music.MusicRepository

interface MusicInteractor {
    suspend fun loadMusicsFromDeviceStorage()
}

class MusicInteractorImpl(
    private val musicRepository: MusicRepository
) : MusicInteractor {
    override suspend fun loadMusicsFromDeviceStorage() = musicRepository.loadMusicsFromDeviceStorage()
}