package com.goesplayer.view.player.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.goesplayer.commons.data.Result
import com.goesplayer.music.data.model.Music
import com.goesplayer.view.player.MediaPlayerClient
import com.goestech.lyrics.data.model.Lyrics
import com.goestech.lyrics.data.repository.LyricsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
class PlayerFragmentViewModel(
    private val lyricsRepository: LyricsRepository,
    private val mediaPlayerClient: MediaPlayerClient
) : ViewModel() {
    val music: LiveData<Music> = mediaPlayerClient.musicFlow.asLiveData(viewModelScope.coroutineContext)
    val position: LiveData<Long> = mediaPlayerClient.positionFlow.asLiveData(viewModelScope.coroutineContext)
    val isPlaying: LiveData<Boolean> = mediaPlayerClient.isPlayingFlow.asLiveData(viewModelScope.coroutineContext)
    val lyrics = MutableLiveData<Lyrics?>(null)

    fun onStart() {
        mediaPlayerClient.onStart()
    }

    fun onStop() {
        mediaPlayerClient.onStop()
    }

    fun skipToNext() {
        mediaPlayerClient.skipToNext()
    }

    fun skipToPrevious() {
        mediaPlayerClient.skipToPrevious()
    }

    fun playOrPause() {
        mediaPlayerClient.playOrPause()
    }

    fun seekTo(progress: Int) {
        mediaPlayerClient.seekTo(progress.toLong())
    }

    fun loadLyrics() {
        viewModelScope.launch {
            val music = music.value ?: return@launch
            val musicName = music.title ?: music.displayName
            ?: music.fileName.substringBeforeLast(".")
            val artist = music.artist ?: return@launch

            val lyricsResult = when (val result = lyricsRepository.getLyrics(musicName, artist)) {
                is Result.Success -> result.value
                else -> null
            }
            lyrics.postValue(lyricsResult)
        }
    }
}