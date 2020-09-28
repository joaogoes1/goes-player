package com.goestech.goesplayer.view.player

import android.content.ComponentName
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.goestech.goesplayer.player.PlayerService

@Suppress("MemberVisibilityCanBePrivate")
abstract class BasePlayerFragment : Fragment() {
    protected lateinit var mediaBrowser: MediaBrowserCompat
    protected var controllerCallback = object : MediaControllerCompat.Callback() {

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {}

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {}
    }
    protected val connectionCallbacks = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {

            mediaBrowser.sessionToken.also { token ->
                val mediaController = MediaControllerCompat(
                    requireContext(),
                    token
                )
                MediaControllerCompat.setMediaController(requireActivity(), mediaController)
            }
        }

        override fun onConnectionSuspended() {
            // The Service has crashed. Disable transport controls until it automatically reconnects
        }

        override fun onConnectionFailed() {
            // The Service has refused our connection
        }
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaBrowser = MediaBrowserCompat(
            requireContext(),
            ComponentName(requireContext(), PlayerService::class.java),
            connectionCallbacks,
            null // optional Bundle
        )
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        mediaBrowser.connect()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        //volumeControlStream = AudioManager.STREAM_MUSIC
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        MediaControllerCompat
            .getMediaController(requireActivity())
            ?.unregisterCallback(controllerCallback)
        mediaBrowser.disconnect()
    }

    // TODO: Improve UI initialization
    fun buildTransportControls(view: View) {
        val mediaController = MediaControllerCompat.getMediaController(requireActivity())
        view.setOnClickListener {
            val pbState = mediaController.playbackState.state
            if (pbState == PlaybackStateCompat.STATE_PLAYING) {
                mediaController.transportControls.pause()
            } else {
                mediaController.transportControls.play()
            }
        }

        // Display the initial state
        val metadata = mediaController.metadata
        val pbState = mediaController.playbackState

        // Register a Callback to stay in sync
        mediaController.registerCallback(controllerCallback)
    }
}