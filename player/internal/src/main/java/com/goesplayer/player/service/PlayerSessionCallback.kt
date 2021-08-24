package com.goesplayer.player.service

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.session.MediaButtonReceiver
import com.goesplayer.music.data.model.Music
import com.goesplayer.music.data.repository.music.MusicRepository
import com.goesplayer.player.R
import com.goesplayer.player.mapper.createMediaMetadataCompat
import com.goesplayer.player.mapper.toMediaItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.FileNotFoundException

private const val PLAYER_ERROR_TAG = "GOES PLAYER-MEDIAPLAYER"
private const val NOTIFICATION_ID = 23456789

class PlayerSessionCallback(
    private val service: PlayerService,
    private val mediaSession: MediaSessionCompat,
    private val musicRepository: MusicRepository,
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
        // Given a media session and its context (usually the component containing the session)
        // Create a NotificationCompat.Builder

        // Get the session's metadata
        val controller = mediaSession.controller
        val mediaMetadata = controller.metadata
        val description = mediaMetadata.description

        val builder = NotificationCompat.Builder(service.applicationContext, NOTIFICATION_ID.toString()).apply {
            // Add the metadata for the currently playing track
            setContentTitle(description.title)
            setContentText(description.subtitle)
            setSubText(description.description)
            setLargeIcon(description.iconBitmap)

            // Enable launching the player by clicking the notification
            setContentIntent(controller.sessionActivity)

            // Stop the service when the notification is swiped away
            setDeleteIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    service.applicationContext,
                    PlaybackStateCompat.ACTION_STOP
                )
            )

            // Make the transport controls visible on the lockscreen
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            // Add an app icon and set its accent color
            // Be careful about the color
            setSmallIcon(R.drawable.ic_pause_white)
            color = ContextCompat.getColor(service.applicationContext, R.color.design_default_color_primary_dark)

            // Add a pause button
            addAction(
                NotificationCompat.Action(
                    R.drawable.ic_pause_white,
                    "Pausar",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        service.applicationContext,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE
                    )
                )
            )

            // Take advantage of MediaStyle features
            setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(mediaSession.sessionToken)
                .setShowActionsInCompactView(0)

                // Add a cancel button
                .setShowCancelButton(true)
                .setCancelButtonIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        service.applicationContext,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )
            )
        }

        // Display the notification and place the service in the foreground
        service.startForeground(NOTIFICATION_ID, builder.build())
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