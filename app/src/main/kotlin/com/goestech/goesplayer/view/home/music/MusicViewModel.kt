package com.goestech.goesplayer.view.home.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goestech.goesplayer.data.entity.Music
import com.goestech.goesplayer.data.repository.music.MusicRepository
import com.goestech.goesplayer.data.repository.playlist.PlaylistRepository
import kotlinx.coroutines.launch

class MusicViewModel(
    private val musicRepository: MusicRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {
    val musics = MutableLiveData<List<Music>>()

    fun loadMusics() {
        viewModelScope.launch {
            val musicList = musicRepository.getAllMusics()
            musics.postValue(musicList)
        }
    }

    fun playMusic(music: Music) {
        viewModelScope.launch {
            musicRepository.playMusic(music, musics.value ?: emptyList())
        }
    }
}