package com.goestech.goesplayer.view.home.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goestech.goesplayer.bussiness.interactor.MusicInteractor
import com.goestech.goesplayer.bussiness.model.MusicModel
import kotlinx.coroutines.launch

class MusicViewModel(
    private val musicInteractor: MusicInteractor
) : ViewModel() {
    val musics = MutableLiveData<List<MusicModel>>()

    fun loadMusics() {
        viewModelScope.launch {
            val musicList = musicInteractor.getAllMusics()
            musics.postValue(musicList)
        }
    }

    fun playMusic(music: MusicModel) {
        TODO("Not yet implemented")
    }
}