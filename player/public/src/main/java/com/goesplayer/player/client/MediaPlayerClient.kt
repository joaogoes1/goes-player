package com.goesplayer.player.client

import com.goesplayer.music.data.model.Music
import kotlinx.coroutines.flow.Flow

interface MediaPlayerClient {
    val isPlaying: Boolean
    val currentMusic: Music?
    val musicFlow: Flow<Music>
    val positionFlow: Flow<Long>
    val isPlayingFlow: Flow<Boolean>

    fun onCreate()
    fun onStart()
    fun onStop()

    fun playMusic(musicId: String)
    fun skipToPrevious()
    fun skipToNext()
    fun play()
    fun pause()
    fun playOrPause()
    fun seekTo(progress: Long)
}