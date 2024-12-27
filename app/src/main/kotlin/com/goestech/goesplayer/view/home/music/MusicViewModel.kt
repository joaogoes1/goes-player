package com.goesplayer.view.home.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goesplayer.music.data.model.Music
import com.goesplayer.music.data.repository.music.MusicRepository
import com.goesplayer.music.data.repository.playlist.PlaylistRepository
import com.goesplayer.view.player.MediaPlayerClient
import kotlinx.coroutines.launch

class MusicViewModel(
    private val musicRepository: MusicRepository,
    private val playlistRepository: PlaylistRepository,
    private val mediaPlayerClient: MediaPlayerClient
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
        mediaPlayerClient.playMusic(music.musicId.toString())
    }
}