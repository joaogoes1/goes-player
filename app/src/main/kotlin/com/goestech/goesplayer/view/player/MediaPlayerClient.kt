package com.goestech.goesplayer.view.player

import android.content.ComponentName
import android.content.Context
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.viewModelScope
import com.goestech.goesplayer.data.entity.Music
import com.goestech.goesplayer.player.PlayerService
import com.goestech.goesplayer.player.toMusic
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

@FlowPreview
@ExperimentalCoroutinesApi
class MediaPlayerClient(
    private val context: Context
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {
    private val callbackList: MutableList<MediaControllerCompat.Callback> = mutableListOf()
    private val mediaBrowserConnectionCallback: MediaBrowserConnectionCallback = MediaBrowserConnectionCallback()
    private val mediaControllerCallback: MediaControllerCallback = MediaControllerCallback()
    private val mediaBrowserSubscriptionCallback: MediaBrowserSubscriptionCallback = MediaBrowserSubscriptionCallback()
    private var mediaBrowser: MediaBrowserCompat? = null
    private var mediaController: MediaControllerCompat? = null
    val isPlaying: Boolean
        get() = mediaController?.playbackState?.state == PlaybackStateCompat.STATE_PLAYING
    val music: Music?
        get() = mediaController?.metadata?.toMusic()
    private val _musicFlow = ConflatedBroadcastChannel<MediaMetadataCompat>()
    val musicFlow: Flow<Music> = _musicFlow.asFlow().map { it.toMusic() }
    private val positionChannel = ConflatedBroadcastChannel<Long>()
    val positionFlow: Flow<Long> = positionChannel.asFlow()

    fun playMusic(musicId: String) {
        mediaController?.transportControls?.playFromMediaId(musicId, null)
    }

    fun onStart() {
        if (mediaBrowser == null) {
            mediaBrowser = MediaBrowserCompat(
                context,
                ComponentName(context, PlayerService::class.java),
                mediaBrowserConnectionCallback,
                null)
            mediaBrowser?.connect()
            launch(newCoroutineContext(Dispatchers.Default)) {
                while (true) {
                    positionChannel.send(mediaController?.playbackState?.position ?: 0L)
                    val waitTime: Long = 1000.times(mediaController?.playbackState?.playbackSpeed ?: 1f).toLong()
                    delay(waitTime)
                }
            }
        }
    }

    fun onStop() {
        if (mediaController != null) {
            mediaController?.unregisterCallback(mediaControllerCallback)
            mediaController = null
        }
        if (mediaBrowser != null && mediaBrowser?.isConnected == true) {
            mediaBrowser?.disconnect()
            mediaBrowser = null
        }
        resetState()
    }

    private fun resetState() {
        performOnAllCallbacks(object : CallbackCommand {
            override fun perform(callback: MediaControllerCompat.Callback) {
                callback.onPlaybackStateChanged(null)
            }
        })
    }

    fun registerCallback(callback: MediaControllerCompat.Callback) {
        callbackList.add(callback)

        mediaController?.let { mMediaController ->
            val metadata = mMediaController.metadata
            if (metadata != null) {
                callback.onMetadataChanged(metadata)
            }
            val playbackState = mMediaController.playbackState
            if (playbackState != null) {
                callback.onPlaybackStateChanged(playbackState)
            }
        }

    }

    private fun performOnAllCallbacks(command: CallbackCommand) {
        for (callback in callbackList) {
            command.perform(callback)
        }
    }


    private interface CallbackCommand {
        fun perform(callback: MediaControllerCompat.Callback)
    }

    private inner class MediaBrowserConnectionCallback : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            try {
                mediaBrowser?.let { mMediaBrowser ->
                    mediaController = MediaControllerCompat(context, mMediaBrowser.sessionToken)
                        .apply {
                            registerCallback(mediaControllerCallback)
//                            mediaControllerCallback.onMetadataChanged(metadata)
                            mediaControllerCallback.onPlaybackStateChanged(playbackState)
                        }
                }
            } catch (e: RemoteException) {
                throw RuntimeException(e)
            }
            mediaBrowser?.let { mMediaBrowser ->
                mMediaBrowser.subscribe(mMediaBrowser.root, mediaBrowserSubscriptionCallback)
            }
        }
    }

    inner class MediaBrowserSubscriptionCallback : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(parentId: String,
                                      children: List<MediaBrowserCompat.MediaItem>) {
            for (mediaItem in children)
                mediaController?.addQueueItem(mediaItem.description)
            mediaController?.transportControls?.prepare()
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onMetadataChanged(metadata: MediaMetadataCompat) {
            performOnAllCallbacks(object : CallbackCommand {
                override fun perform(callback: MediaControllerCompat.Callback) {
                    callback.onMetadataChanged(metadata)
                }
            })
            launch {
                _musicFlow.send(metadata)
            }
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            performOnAllCallbacks(object : CallbackCommand {
                override fun perform(callback: MediaControllerCompat.Callback) {
                    callback.onPlaybackStateChanged(state)
                }
            })
        }

        override fun onSessionDestroyed() {
            resetState()
            onPlaybackStateChanged(null)
        }
    }

    fun skipToPrevious() {
        mediaController?.transportControls?.skipToPrevious()
    }

    fun skipToNext() {
        mediaController?.transportControls?.skipToNext()
    }

    fun play() {
        mediaController?.transportControls?.play()
    }

    fun pause() {
        mediaController?.transportControls?.pause()
    }

    fun playOrPause() {
        if (mediaController?.playbackState?.state == PlaybackStateCompat.STATE_PLAYING) {
            pause()
        } else {
            play()
        }
    }
}