package com.goestech.goesplayer.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.browse.MediaBrowser
import android.os.Bundle
import android.service.media.MediaBrowserService
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat

private const val MEDIA_SESSION_TAG = "goes-player-session"

// TODO: Refactor that. Inject all is possible
// TODO: Implement notifications
class PlayerService : MediaBrowserServiceCompat() {

    private val player = MediaPlayer()
    private lateinit var session: MediaSessionCompat
    private val sessionCallback = PlayerCallback()
    private val playbackStateBuilder = PlaybackStateCompat.Builder()
    private val mediaMetadataBuilder = MediaMetadataCompat.Builder()

    override fun onCreate() {
        super.onCreate()
        initializeMediaSession()
    }

    private fun initializeMediaSession() {
        session = MediaSessionCompat(applicationContext, MEDIA_SESSION_TAG)
        playbackStateBuilder
            .setActions(
                PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_PLAY_PAUSE
            )
        session.setPlaybackState(playbackStateBuilder.build())
        session.setCallback(sessionCallback)
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? =
        BrowserRoot(MEDIA_SESSION_TAG, null)


    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        return if (parentId == MEDIA_SESSION_TAG) {
            result.sendResult(null)
        } else {
            TODO("Implement this")
        }
    }

    private inner class PlayerCallback : MediaSessionCompat.Callback(), AudioManager.OnAudioFocusChangeListener {

        val audioManager: AudioManager
            get() = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
                    onPause()
                }
            }
        }
        private val intentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        //TODO: Think on Design Pattern to remove the "version ifs" and turn this more safe
        private lateinit var audioFocusRequest: AudioFocusRequest

        override fun onPlay() {
            val result: Int = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
                        setOnAudioFocusChangeListener(this@PlayerCallback)
                        setAudioAttributes(AudioAttributes.Builder().run {
                            setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            build()
                        })
                        build()
                    }
                audioManager.requestAudioFocus(audioFocusRequest)
            } else {
                @Suppress("DEPRECATION")
                audioManager.requestAudioFocus(
                    this,
                    AudioAttributes.CONTENT_TYPE_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
                )
            }

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                startService(Intent(applicationContext, MediaBrowserService::class.java))
                session.isActive = true
                player.start()
                registerReceiver(broadcastReceiver, intentFilter)
            }
        }

        override fun onPause() {
            player.pause()
            unregisterReceiver(broadcastReceiver)
        }

        override fun onStop() {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                audioManager.abandonAudioFocusRequest(audioFocusRequest)
            } else {
                @Suppress("DEPRECATION")
                audioManager.abandonAudioFocus(this)
            }
            stopSelf()
            session.isActive = false
            player.stop()
            unregisterReceiver(broadcastReceiver)
        }

        override fun onAudioFocusChange(focusChange: Int) {
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> onPlay()
                AudioManager.AUDIOFOCUS_LOSS -> onStop()
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> onPause()
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> onPause()
            }
        }
    }
}