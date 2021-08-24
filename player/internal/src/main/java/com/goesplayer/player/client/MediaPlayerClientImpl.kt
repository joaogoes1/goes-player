package com.goesplayer.player.client

import android.content.ComponentName
import android.content.Context
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.goesplayer.music.data.model.Music
import com.goesplayer.player.mapper.toMusic
import com.goesplayer.player.service.PlayerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class MediaPlayerClientImpl(
    private val context: Context
) : MediaPlayerClient, CoroutineScope by CoroutineScope(Dispatchers.Main) {
    private val mediaBrowserConnectionCallback = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            Log.e("Player connection", "Connection connected")
            try {
                mediaBrowser?.let { mMediaBrowser ->
                    mediaController = MediaControllerCompat(context, mMediaBrowser.sessionToken)
                    mediaController?.registerCallback(mediaControllerCallback)
                }
            } catch (e: RemoteException) {
                throw RuntimeException(e)
            }
            mediaBrowser?.let { mMediaBrowser ->
                mMediaBrowser.subscribe(mMediaBrowser.root, mediaBrowserSubscriptionCallback)
            }
        }

        override fun onConnectionSuspended() {
            Log.e("Player connection", "Connection suspended")
        }

        override fun onConnectionFailed() {
            Log.e("Player connection", "Connection failed")
        }
    }
    private val mediaControllerCallback = object : MediaControllerCompat.Callback() {
        override fun onMetadataChanged(metadata: MediaMetadataCompat) {
            launch {
                _musicFlow.send(metadata)
            }
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            launch {
                positionChannel.send(state?.position ?: 0L)
                isPlayingChannel.send(state?.state == PlaybackStateCompat.STATE_PLAYING)
            }
        }

        override fun onSessionDestroyed() {
            onPlaybackStateChanged(null)
        }
    }
    private val mediaBrowserSubscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: List<MediaBrowserCompat.MediaItem>
        ) {
            for (mediaItem in children)
                mediaController?.addQueueItem(mediaItem.description)
            mediaController?.transportControls?.prepare()
        }
    }
    private var mediaBrowser: MediaBrowserCompat? = null
    private var mediaController: MediaControllerCompat? = null
    override val isPlaying: Boolean
        get() = mediaController?.playbackState?.state == PlaybackStateCompat.STATE_PLAYING
    override val currentMusic: Music?
        get() = mediaController?.metadata?.toMusic()
    private val _musicFlow = ConflatedBroadcastChannel<MediaMetadataCompat>()
    override val musicFlow: Flow<Music> = _musicFlow.asFlow().map { it.toMusic() }

    // TODO: Unificar position e isPlaying num objeto de PlaybackState
    private val positionChannel = ConflatedBroadcastChannel<Long>()
    override val positionFlow: Flow<Long> = positionChannel.asFlow()
    private val isPlayingChannel = ConflatedBroadcastChannel<Boolean>()
    override val isPlayingFlow: Flow<Boolean> = isPlayingChannel.asFlow()

    override fun playMusic(musicId: String) {
        mediaController?.transportControls?.playFromMediaId(musicId, null)
    }

    override fun onCreate() {
        if (mediaBrowser == null) {
            mediaBrowser = MediaBrowserCompat(
                context,
                ComponentName(
                    context,
                    PlayerService::class.java
                ),
                mediaBrowserConnectionCallback,
                null
            )
        }
    }

    override fun onStart() {
        if (mediaBrowser?.isConnected == false)
            mediaBrowser?.connect()
    }

    override fun onStop() {
        if (mediaController != null) {
            mediaController?.unregisterCallback(mediaControllerCallback)
            mediaController = null
        }
        if (mediaBrowser != null && mediaBrowser?.isConnected == true) {
            mediaBrowser?.disconnect()
            mediaBrowser = null
        }
    }

    override fun skipToPrevious() {
        mediaController?.transportControls?.skipToPrevious()
    }

    override fun skipToNext() {
        mediaController?.transportControls?.skipToNext()
    }

    override fun play() {
        mediaController?.transportControls?.play()
    }

    override fun pause() {
        mediaController?.transportControls?.pause()
    }

    override fun playOrPause() {
        if (mediaController?.playbackState?.state == PlaybackStateCompat.STATE_PLAYING) {
            pause()
        } else {
            play()
        }
    }

    override fun seekTo(progress: Long) {
        mediaController?.transportControls?.seekTo(progress)
    }
}