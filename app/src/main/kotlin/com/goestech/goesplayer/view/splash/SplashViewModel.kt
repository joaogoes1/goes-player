package com.goesplayer.view.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goesplayer.music.data.repository.music.MusicRepository
import kotlinx.coroutines.launch

class SplashViewModel(
    private val musicRepository: MusicRepository
) : ViewModel() {
    val status = MutableLiveData(InitializationStatus.UNINITIALIZED)

    fun initApp() {
        status.postValue(InitializationStatus.INITIALIZING)
        viewModelScope.launch {
            musicRepository.loadMusicsFromDeviceStorage()
                .onSuccess {
                    status.postValue(InitializationStatus.READY)
                }.onError {

                }
        }
    }
}

enum class InitializationStatus {
    UNINITIALIZED,
    INITIALIZING,
    READY
}