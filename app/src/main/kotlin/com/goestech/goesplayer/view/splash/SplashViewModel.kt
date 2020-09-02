package com.goestech.goesplayer.view.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goestech.goesplayer.bussiness.interactor.MusicInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val musicInteractor: MusicInteractor
) : ViewModel() {
    val status = MutableLiveData(InitializationStatus.UNINITIALIZED)

    fun initApp() {
        status.postValue(InitializationStatus.INITIALIZING)
        viewModelScope.launch {
            //musicInteractor.loadMusicsFromDeviceStorage()
            delay(5000)
        }
        status.postValue(InitializationStatus.READY)
    }
}

enum class InitializationStatus {
    UNINITIALIZED,
    INITIALIZING,
    READY
}