package com.goestech.goesplayer.view.player

import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_NONE
import com.goestech.goesplayer.data.repository.music.MusicRepository
import com.goestech.goesplayer.player.createMediaMetadataCompat
import com.goestech.goesplayer.player.toMediaItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PlayerSessionCallback(
    private val mediaSession: MediaSessionCompat,
    private val player: Player,
    private val musicRepository: MusicRepository
) : MediaSessionCompat.Callback(), CoroutineScope by CoroutineScope(Dispatchers.Default) {
    
    private val playlist = mutableListOf<MediaSessionCompat.QueueItem>()
    private val reproductionOrder = mutableListOf<Int>()
    private var position = 0
    private var preparedMedia: MediaMetadataCompat? = null

    override fun onAddQueueItem(description: MediaDescriptionCompat?) {
        playlist.add(MediaSessionCompat.QueueItem(description, description.hashCode().toLong()))
        position = if (position == -1) 0 else position
        mediaSession.setQueue(playlist)
    }

    override fun onRemoveQueueItem(description: MediaDescriptionCompat?) {
        playlist.remove(MediaSessionCompat.QueueItem(description, description.hashCode().toLong()))
        position = if (playlist.isEmpty()) -1 else position
        mediaSession.setQueue(playlist)
    }

    override fun onPrepare() {
        if (position < 0 && playlist.isEmpty()) {
            return
        }

        val mediaId = playlist.getOrNull(position)?.description?.mediaId
        mediaId?.let {
            launch(coroutineContext) {
                 preparedMedia = musicRepository.getMusic(mediaId.toLongOrNull() ?: 0).createMediaMetadataCompat()
            }
            mediaSession.setMetadata(preparedMedia)

            if (!mediaSession.isActive) {
                mediaSession.isActive = true
            }
        }
    }

    override fun onPlay() {
        preparedMedia?.let {
            player.playFromMedia(it)
        } ?: onPrepare()
    }

    override fun onPause() {
        player.pause()
    }

    override fun onStop() {
        player.stop()
        mediaSession.isActive = false
    }

    override fun onSkipToNext() {
        position = ++position % playlist.size
        preparedMedia = null
        onPlay()
    }

    override fun onSkipToPrevious() {
        position = if (position > 0) position - 1 else playlist.size - 1
        preparedMedia = null
        onPlay()
    }

    override fun onSeekTo(pos: Long) {
        player.seekTo(pos)
    }

    override fun onSetShuffleMode(shuffleMode: Int) {
        TODO("Terminar de implementar isso")
        if (shuffleMode == SHUFFLE_MODE_NONE) {
            reproductionOrder.shuffle()
        } else {
            reproductionOrder.sort()
        }
    }
}