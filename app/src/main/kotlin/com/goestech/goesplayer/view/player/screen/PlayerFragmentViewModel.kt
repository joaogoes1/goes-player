package com.goestech.goesplayer.view.player.screen

import androidx.lifecycle.*
import com.goestech.goesplayer.data.entity.Music
import com.goestech.goesplayer.view.player.MediaPlayerClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.newCoroutineContext
import java.sql.Timestamp

@ExperimentalCoroutinesApi
@FlowPreview
class PlayerFragmentViewModel(
    private val mediaPlayerClient: MediaPlayerClient
) : ViewModel() {
    val music: LiveData<Music> = mediaPlayerClient.musicFlow.asLiveData(viewModelScope.coroutineContext)
    val position: LiveData<Long> = mediaPlayerClient.positionFlow.asLiveData(viewModelScope.coroutineContext)
    val isPlaying: LiveData<Boolean> = mediaPlayerClient.isPlayingFlow.asLiveData(viewModelScope.coroutineContext)

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
}