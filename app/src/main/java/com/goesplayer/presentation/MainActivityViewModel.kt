package com.goesplayer.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import com.goesplayer.BancoController
import com.goesplayer.data.repository.MusicRepository
import com.goesplayer.data.model.Music
import com.goesplayer.data.model.Playlist
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
) : ViewModel() {

    var crud: BancoController? = null
    var controller: MediaController? = null
    val songList: MutableLiveData<List<Music>> = MutableLiveData()
    val currentMusic: MutableLiveData<Music?> = MutableLiveData()
    val isPlaying: MutableState<Boolean> = mutableStateOf(false)

    fun loadSongs() {
        songList.postValue(musicRepository.loadSongs())
    }

    fun playSong(music: Music) {
        controller?.setMediaItem(
            MediaItem
            .Builder()
            .setUri(music.songUri)
            .setMediaMetadata(
                MediaMetadata
                .Builder()
                .setArtworkUri(music.albumArtUri)
                .build()
            ).build())
        controller?.prepare()
        controller?.play()
    }

    fun playOrPause() {
        if (isPlaying.value) {
            controller?.pause()
        } else {
            controller?.play()
        }
    }
}
