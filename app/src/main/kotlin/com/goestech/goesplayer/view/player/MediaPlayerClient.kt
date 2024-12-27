package com.goesplayer.view.player

import android.content.ComponentName
import android.content.Context
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.goesplayer.music.data.model.Music
import com.goesplayer.player.PlayerService
import com.goesplayer.player.toMusic
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
class MediaPlayerClient(
    private val context: Context
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {
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

    // TODO: Unificar position e isPlaying num objeto de PlaybackState
    private val positionChannel = ConflatedBroadcastChannel<Long>()
    val positionFlow: Flow<Long> = positionChannel.asFlow()
    private val isPlayingChannel = ConflatedBroadcastChannel<Boolean>()
    val isPlayingFlow: Flow<Boolean> = isPlayingChannel.asFlow()

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
        override fun onChildrenLoaded(
            parentId: String,
            children: List<MediaBrowserCompat.MediaItem>
        ) {
            for (mediaItem in children)
                mediaController?.addQueueItem(mediaItem.description)
            mediaController?.transportControls?.prepare()
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
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

    fun seekTo(progress: Long) {
        mediaController?.transportControls?.seekTo(progress)
    }
}