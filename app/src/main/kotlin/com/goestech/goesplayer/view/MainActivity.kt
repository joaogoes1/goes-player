package com.goesplayer.view

import android.content.ComponentName
import android.media.AudioManager
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.goesplayer.R
import com.goesplayer.music.data.model.Music
import com.goesplayer.player.PlayerService
import com.goesplayer.databinding.HomeActivityBinding

class MainActivity : AppCompatActivity() {

    private var binding: HomeActivityBinding? = null
    private lateinit var mediaBrowser: MediaBrowserCompat
    private var controllerCallback: MediaControllerCompat.Callback = object : MediaControllerCompat.Callback() {

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {}

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {}
    }
    private val connectionCallbacks = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaBrowser.sessionToken.also { token ->
                val mediaController = MediaControllerCompat(
                    this@MainActivity,
                    token
                )
                MediaControllerCompat.setMediaController(this@MainActivity, mediaController)
            }
            MediaControllerCompat.getMediaController(this@MainActivity).registerCallback(controllerCallback)
        }

        override fun onConnectionSuspended() {
            // The Service has crashed. Disable transport controls until it automatically reconnects
        }

        override fun onConnectionFailed() {
            // The Service has refused our connection
        }
    }
    private val transportControls: MediaControllerCompat.TransportControls?
        get() = MediaControllerCompat.getMediaController(this)?.transportControls

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity)
        mediaBrowser = MediaBrowserCompat(
            this,
            ComponentName(this, PlayerService::class.java),
            connectionCallbacks,
            null
        )
    }

    override fun onStart() {
        super.onStart()
        mediaBrowser.connect()
    }

    override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    public override fun onStop() {
        super.onStop()
        MediaControllerCompat.getMediaController(this)?.unregisterCallback(controllerCallback)
        mediaBrowser.disconnect()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    fun playMedia(music: Music) {
        transportControls?.playFromMediaId(music.musicId.toString(), null)
    }

    fun playOrPause() {
        val pbState = mediaController.playbackState?.state
        if (pbState == PlaybackStateCompat.STATE_PLAYING) {
            transportControls?.pause()
        } else {
            transportControls?.play()
        }
    }

    fun stop() {
        transportControls?.stop()
    }

    fun skipToNext() {
        transportControls?.skipToNext()
    }

    fun skipToPrevious() {
        transportControls?.skipToPrevious()
    }
}