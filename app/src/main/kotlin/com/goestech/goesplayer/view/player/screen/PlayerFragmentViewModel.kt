package com.goestech.goesplayer.view.player.screen

import android.support.v4.media.session.PlaybackStateCompat
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

    fun playOrPause() {
        if (mediaPlayerClient.isPlaying) {
            mediaPlayerClient.pause()
        } else {
            mediaPlayerClient.play()
        }
    }
}