package com.goestech.goesplayer.view.home.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goestech.goesplayer.data.model.Music
import com.goestech.goesplayer.data.repository.music.MusicRepository
import com.goestech.goesplayer.data.repository.playlist.PlaylistRepository
import com.goestech.goesplayer.view.player.MediaPlayerClient
import kotlinx.coroutines.launch

class MusicViewModel(
    private val musicRepository: MusicRepository,
    private val playlistRepository: PlaylistRepository,
    private val musicConnection: MediaPlayerClient
) : ViewModel() {
    val musics = MutableLiveData<List<Music>>()

    fun loadMusics() {
        viewModelScope.launch {
            val musicList = musicRepository.getAllMusics()
            musics.postValue(musicList)
        }
    }

    fun playMusic(music: Music) {
//        viewModelScope.launch {
//            playlistRepository.saveCurrentPlaylist(musics.value ?: emptyList())
//        }
        musicConnection.playMusic(music.musicId.toString())
    }
}