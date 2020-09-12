package com.goestech.goesplayer.view.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goestech.goesplayer.data.repository.music.MusicRepository
import kotlinx.coroutines.launch

class SplashViewModel(
    private val musicmusicRepository: MusicRepository
) : ViewModel() {
    val status = MutableLiveData(InitializationStatus.UNINITIALIZED)

    fun initApp() {
        status.postValue(InitializationStatus.INITIALIZING)
        viewModelScope.launch {
            musicmusicRepository.loadMusicsFromDeviceStorage()
                .run {
                    status.postValue(InitializationStatus.READY)
                }
        }
    }
}

enum class InitializationStatus {
    UNINITIALIZED,
    INITIALIZING,
    READY
}