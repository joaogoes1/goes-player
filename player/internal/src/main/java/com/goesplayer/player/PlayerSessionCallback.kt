package com.goesplayer.player

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.goesplayer.music.data.model.Music
import com.goesplayer.music.data.repository.music.MusicRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.FileNotFoundException

private const val PLAYER_ERROR_TAG = "GOES PLAYER-MEDIAPLAYER"

class PlayerSessionCallback(
    private val mediaSession: MediaSessionCompat,
    private val musicRepository: MusicRepository
) : MediaSessionCompat.Callback(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, CoroutineScope by CoroutineScope(Dispatchers.Default) {

    private val player = MediaPlayer().apply {
        setOnPreparedListener(this@PlayerSessionCallback)
        setOnErrorListener(this@PlayerSessionCallback)
    }
    private val playlist = mutableListOf<MediaSessionCompat.QueueItem>()
    private val reproductionOrder = mutableListOf<Int>()
    private var playlistPosition = 0
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
        playlistPosition = if (playlistPosition == -1) 0 else playlistPosition
        mediaSession.setQueue(playlist)
    }

    override fun onRemoveQueueItem(description: MediaDescriptionCompat?) {
        playlist.remove(MediaSessionCompat.QueueItem(description, description.hashCode().toLong()))
        playlistPosition = if (playlist.isEmpty()) -1 else playlistPosition
        mediaSession.setQueue(playlist)
    }

    override fun onPrepare() {
        mediaId?.let {
            launch {
                musicRepository.getMusic(mediaId?.toLongOrNull() ?: 0).onSuccess {
                    preparedMedia = it.createMediaMetadataCompat()
                    mediaSession.setMetadata(preparedMedia)
                    if (!mediaSession.isActive) {
                        mediaSession.isActive = true
                    }
                    onPlay()
                }
            }
        }
    }

    override fun onPlay() {
        preparedMedia?.let { music ->
            player.reset()
            try {
                val uri = Uri.parse(music.getString(METADATA_KEY_PATH))
                    ?: throw FileNotFoundException(music.getString(music.toString()))
                player.setDataSource(uri.path)
                player.prepareAsync()
            } catch (e: Exception) {
                Log.e(PLAYER_ERROR_TAG, "Error on playFromMedia:\n\t" + e.message)
            }
        } ?: onPrepare()
    }

    override fun onPause() {
        player.pause()
        mediaSession.setPlaybackState(
            PlaybackStateCompat
                .Builder()
                .setState(PlaybackStateCompat.STATE_PAUSED, player.currentPosition.toLong(), 1.toFloat())
                .build()
        )
    }

    override fun onStop() {
        player.stop()
        player.release()
        mediaSession.isActive = false
    }

    override fun onSkipToNext() {
        playlistPosition = if (playlistPosition < playlist.size) playlistPosition++ else 0
        preparedMedia = null
        onPlay()
    }

    override fun onSkipToPrevious() {
        playlistPosition = if (playlistPosition > 0) playlistPosition - 1 else playlist.size - 1
        preparedMedia = null
        onPlay()
    }

    override fun onSeekTo(pos: Long) {
        player.seekTo(pos.toInt())
        player.setOnSeekCompleteListener {
            it?.start()
        }
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

    override fun onPrepared(mp: MediaPlayer?) {
        player.start()
        mediaSession.setPlaybackState(
            PlaybackStateCompat
                .Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, player.currentPosition.toLong(), 1.toFloat())
                .build()
        )
        launch(Dispatchers.Default) {
            while (true) {
                val isPlaying = try {
                    player.isPlaying
                } catch (e: Exception) {
                    false
                }

                if (isPlaying) {
                    updateCurrentPosition()
                    val waitTime: Long = 1000//.times(mediaController?.playbackState?.playbackSpeed ?: 1f).toLong()
                    delay(waitTime)
                }
            }
        }
    }

    private fun updateCurrentPosition() {
        val position = player.currentPosition.toLong()
        val range = 1.toFloat()
        val state = if (player.isPlaying)
            PlaybackStateCompat.STATE_PLAYING
        else
            PlaybackStateCompat.STATE_PLAYING

        mediaSession.setPlaybackState(
            PlaybackStateCompat
                .Builder()
                .setState(state, position, range)
                .build()
        )
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Log.e(PLAYER_ERROR_TAG, "What: $what\nExtra: $extra\n")
        mp?.reset()
        mediaSession.setPlaybackState(
            PlaybackStateCompat
                .Builder()
                .setState(PlaybackStateCompat.STATE_ERROR, 0L, 0f)
                .build()
        )
        return true
    }
}