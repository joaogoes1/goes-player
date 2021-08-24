package com.goesplayer.player.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.media.session.MediaSession
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.goesplayer.music.data.model.Music
import com.goesplayer.music.data.repository.music.MusicRepository
import com.goesplayer.music.data.repository.playlist.PlaylistRepository
import com.goesplayer.player.R
import com.goesplayer.player.mapper.createMediaMetadataCompat
import com.goesplayer.player.mapper.toMediaItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

private const val MY_MEDIA_ROOT_ID = "goes-player-session"
const val METADATA_KEY_PATH: String = "android.media.metadata.PATH"
private const val PLAYER_ERROR_TAG = "GOES PLAYER-MEDIAPLAYER"

class PlayerService : MediaBrowserServiceCompat(), CoroutineScope by CoroutineScope(Dispatchers.Default) {

    private lateinit var mediaSession: MediaSessionCompat
    private val playlistRepository: PlaylistRepository by inject()
    private val musicRepository: MusicRepository by inject()
    private val sessionCallback = SessionCallback()
    private val currentPlaylist = mutableListOf<Music>()

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(applicationContext, MY_MEDIA_ROOT_ID)
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS)
        mediaSession.setCallback(sessionCallback)
        sessionToken = mediaSession.sessionToken
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
        sessionCallback.onStop()
        mediaSession.release()
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? =
        BrowserRoot(MY_MEDIA_ROOT_ID, null)

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        result.sendResult(currentPlaylist.map { it.toMediaItem() }.toMutableList())
    }

    private inner class SessionCallback : MediaSessionCompat.Callback() {
        private val player = MediaPlayer()
        private var currentMusic: MediaMetadataCompat? = null
        private val playbackStateBuilder = PlaybackStateCompat.Builder()
        private val onPreparedListener = MediaPlayer.OnPreparedListener {
            it.start()
            mediaSession.setPlayingState(playbackStateBuilder)
        }
        private val onCompletionListener = MediaPlayer.OnCompletionListener {
            it.stop()
            mediaSession.setStoppedState(playbackStateBuilder)
        }
        private val onErrorListener = MediaPlayer.OnErrorListener { player, what, extra ->
            Log.e(PLAYER_ERROR_TAG, "A error emitted during execution.\n    What: $what\n    Extra: $extra ")
            mediaSession.setStoppedState(playbackStateBuilder)
            player.stop()
            player.release()
            true
        }

        init {
            player.apply {
                setOnPreparedListener(onPreparedListener)
                setOnCompletionListener(onCompletionListener)
                setOnErrorListener(onErrorListener)
            }
        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            launch {
                musicRepository
                    .getMusic(mediaId?.toLongOrNull() ?: 0)
                    .onSuccess { music ->
                        currentMusic = music.createMediaMetadataCompat()
                        mediaSession.setMetadata(currentMusic)
                        try {
                            player.reset()
                            player.setDataSource(music.filePath)
                            player.prepareAsync()
                            mediaSession.setBufferingState(playbackStateBuilder)
                            ContextCompat.startForegroundService(
                                applicationContext,
                                Intent(applicationContext, this@PlayerService.javaClass)
                            )
                            openNotification()
                        } catch (e: Exception) {
                            Log.e(PLAYER_ERROR_TAG, "Failed to prepare music:\n${e.stackTrace}")
                            mediaSession.setErrorState(playbackStateBuilder)
                        }
                    }
            }
        }

        // REFATORAR ISSO
        private fun openNotification() {
            val builder = NotificationCompat.Builder(applicationContext, getChannelId()).apply {
                // Add the metadata for the currently playing track
                setContentTitle(currentMusic?.description?.title)
                setContentText(currentMusic?.description?.subtitle)
                setSubText(currentMusic?.description?.description)
                setLargeIcon(currentMusic?.description?.iconBitmap)

                // Enable launching the player by clicking the notification
                setContentIntent(mediaSession.controller.sessionActivity)

                // Stop the service when the notification is swiped away
                setDeleteIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        applicationContext,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )

                // Make the transport controls visible on the lockscreen
                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                // Add an app icon and set its accent color
                // Be careful about the color
                setSmallIcon(R.drawable.ic_pause_white)
                color = ContextCompat.getColor(applicationContext, R.color.design_default_color_primary_dark)

                // Add a pause button
                addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_pause_white,
                        "Pausar",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            applicationContext,
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
                            applicationContext,
                            PlaybackStateCompat.ACTION_STOP
                        )
                    )
                )
            }

            // Display the notification and place the service in the foreground
            startForeground(1, builder.build())
        }
        private fun getChannelId() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                ""
            }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun createNotificationChannel(channelId: String, channelName: String): String{
            val chan = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE)
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(chan)
            return channelId
        }
    }
}

fun MediaSessionCompat.setPlayingState(playbackStateBuilder: PlaybackStateCompat.Builder, position: Long = 0) {
    setPlaybackState(
        playbackStateBuilder
            .setState(PlaybackStateCompat.STATE_PLAYING, position, 1f)
            .build()
    )
}

fun MediaSessionCompat.setBufferingState(playbackStateBuilder: PlaybackStateCompat.Builder, position: Long = 0) {
    setPlaybackState(
        playbackStateBuilder
            .setState(PlaybackStateCompat.STATE_BUFFERING, position, 1f)
            .build()
    )
}

fun MediaSessionCompat.setStoppedState(playbackStateBuilder: PlaybackStateCompat.Builder, position: Long = 0) {
    setPlaybackState(
        playbackStateBuilder
            .setState(PlaybackStateCompat.STATE_STOPPED, position, 1f)
            .build()
    )
}

fun MediaSessionCompat.setErrorState(playbackStateBuilder: PlaybackStateCompat.Builder) {
    setPlaybackState(
        playbackStateBuilder
            .setState(PlaybackStateCompat.STATE_ERROR, 0, 1f)
            .build()
    )
}

/*
    private inner class SessionCallback : MediaSessionCompat.Callback() {
        private val player = MediaPlayer()
        private var currentMusic: MediaMetadataCompat? = null
        private val playbackStateBuilder = PlaybackStateCompat.Builder()
        private val metadataBuilder = MediaMetadataCompat.Builder()

        init {
            player.setOnPreparedListener { it.start() }
        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            launch {
                musicRepository
                    .getMusic(mediaId?.toLongOrNull() ?: 0)
                    .onSuccess {
                        currentMusic = it.createMediaMetadataCompat()
                        onPrepare()
                    }
            }
        }

        override fun onPrepare() {
            currentMusic?.let { music ->
                mediaSession.setMetadata(currentMusic)
                try {
                    player.reset()
                    val uri = Uri.parse(music.getString(METADATA_KEY_PATH))
                        ?: throw FileNotFoundException(music.getString(music.toString()))
                    val file = File(uri.path ?: "")
                    player.setDataSource(file.absolutePath)
                    player.prepareAsync()
                } catch (e: Exception) {
                    Log.e(PLAYER_ERROR_TAG, "Error on playFromMedia:\n\t" + e.message)
                }
            }
        }

        override fun onPlay() {
            currentMusic?.let { music ->
                playbackStateBuilder
                    .setState(
                        PlaybackStateCompat.STATE_PLAYING,
                        player.currentPosition.toLong(),
                        1f
                    )
                    .build()
                if (!mediaSession.isActive) {
                    mediaSession.isActive = true
                }
                openNotification()
            }
        }

        override fun onPause() {
            player.pause()
            mediaSession.setPlaybackState(
                playbackStateBuilder
                    .setState(
                        PlaybackStateCompat.STATE_PAUSED,
                        player.currentPosition.toLong(),
                        1f
                    )
                    .build()
            )
        }

        override fun onStop() {
            player.stop()
            player.release()
            mediaSession.isActive = false
        }

        private fun openNotification() {
            val builder = NotificationCompat.Builder(applicationContext, getChannelId()).apply {
                // Add the metadata for the currently playing track
                setContentTitle(currentMusic?.description?.title)
                setContentText(currentMusic?.description?.subtitle)
                setSubText(currentMusic?.description?.description)
                setLargeIcon(currentMusic?.description?.iconBitmap)

                // Enable launching the player by clicking the notification
                setContentIntent(mediaSession.controller.sessionActivity)

                // Stop the service when the notification is swiped away
                setDeleteIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        applicationContext,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )

                // Make the transport controls visible on the lockscreen
                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                // Add an app icon and set its accent color
                // Be careful about the color
                setSmallIcon(R.drawable.ic_pause_white)
                color = ContextCompat.getColor(applicationContext, R.color.design_default_color_primary_dark)

                // Add a pause button
                addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_pause_white,
                        "Pausar",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            applicationContext,
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
                            applicationContext,
                            PlaybackStateCompat.ACTION_STOP
                        )
                    )
                )
            }

            // Display the notification and place the service in the foreground
            startForeground(1, builder.build())
        }
        private fun getChannelId() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel("my_service", "My Background Service")
                } else {
                    ""
                }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun createNotificationChannel(channelId: String, channelName: String): String{
            val chan = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE)
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(chan)
            return channelId
        }
    }
 */