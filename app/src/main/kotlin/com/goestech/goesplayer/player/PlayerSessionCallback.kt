package com.goestech.goesplayer.player

import android.os.Bundle
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.goestech.goesplayer.data.entity.Music
import com.goestech.goesplayer.data.repository.music.MusicRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerSessionCallback(
    private val mediaSession: MediaSessionCompat,
    private val player: Player,
    private val musicRepository: MusicRepository
) : MediaSessionCompat.Callback(), CoroutineScope by CoroutineScope(Dispatchers.Default) {

    private val playlist = mutableListOf<MediaSessionCompat.QueueItem>()
    private val reproductionOrder = mutableListOf<Int>()
    private var position = 0
    private var mediaId: String? = null
    private var preparedMedia: MediaMetadataCompat? = null

    fun setPlaylist(newPlaylist: List<Music>) {
        val queue = newPlaylist.map {
            val media = it.toMediaItem()
            MediaSessionCompat.QueueItem(media.description, media.description.hashCode().toLong())
        }
        playlist.clear()
        playlist.addAll(queue)
        mediaSession.setQueue(playlist)
    }

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
        mediaId?.let {
            launch(coroutineContext) {
                preparedMedia = musicRepository.getMusic(mediaId?.toLongOrNull() ?: 0).createMediaMetadataCompat()
                mediaSession.setMetadata(preparedMedia)
                if (!mediaSession.isActive) {
                    mediaSession.isActive = true
                }
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
        position = if (position < playlist.size) position++ else 0
        preparedMedia = null
        onPlay()
    }

    override fun onSkipToPrevious() {
        position = if (position > 0) position - 1 else playlist.size - 1
        preparedMedia = null
        onPlay()
    }

    override fun onSeekTo(pos: Long) {
        player.seekTo(pos.toInt())
    }

    override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
        this.mediaId = mediaId
        onPrepare()
    }

    override fun onSetShuffleMode(shuffleMode: Int) {
        TODO("Terminar de implementar isso")
        if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_NONE) {
            reproductionOrder.shuffle()
        } else {
            reproductionOrder.sort()
        }
    }
}