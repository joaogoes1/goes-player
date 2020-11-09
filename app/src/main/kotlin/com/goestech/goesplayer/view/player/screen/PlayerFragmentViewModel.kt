package com.goestech.goesplayer.view.player.screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.goestech.goesplayer.data.entity.Music
import com.goestech.goesplayer.view.player.MediaPlayerClient
import java.sql.Timestamp

class PlayerFragmentViewModel(
    private val mediaPlayerClient: MediaPlayerClient
) : ViewModel() {

    val currentTime: MutableLiveData<Timestamp> = MutableLiveData()
    val music: MutableLiveData<Music> = MutableLiveData()

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

    fun play() {
        with(mediaPlayerClient) {
            if (isPlaying)
                play()
            else
                pause()
        }
    }

    fun playMusic(music: Music?) {
        music?.let {
            mediaPlayerClient.playMusic(music.musicId.toString())
        }
    }
}