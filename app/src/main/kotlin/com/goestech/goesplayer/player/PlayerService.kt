package com.goestech.goesplayer.player

import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.goestech.goesplayer.data.entity.Music
import com.goestech.goesplayer.data.repository.music.MusicRepository
import com.goestech.goesplayer.data.repository.playlist.PlaylistRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

private const val MY_MEDIA_ROOT_ID = "goes-player-session"
val METADATA_KEY_PATH: String  = "android.media.metadata.PATH"


class PlayerService : MediaBrowserServiceCompat(), CoroutineScope by CoroutineScope(Dispatchers.Default) {

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var player: Player
    private val playlistRepository: PlaylistRepository by inject()
    private val musicRepository: MusicRepository by inject()
    private lateinit var sessionCallback: PlayerSessionCallback
    private val currentPlaylist = mutableListOf<Music>()

    override fun onCreate() {
        super.onCreate()
        player = CustomPlayer(applicationContext)
        mediaSession = MediaSessionCompat(applicationContext, MY_MEDIA_ROOT_ID)
        sessionCallback = PlayerSessionCallback(mediaSession, player, musicRepository)
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS)
        mediaSession.setCallback(sessionCallback)
        sessionToken = mediaSession.sessionToken
//        launch {
//            playlistRepository.getCurrentPlaylist().apply {
//                currentPlaylist.addAll(this)
//                sessionCallback.setPlaylist(this)
//            }
//        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
//        mMediaNotificationManager.onDestroy()
        player.stop()
        mediaSession.release()
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? =
        BrowserRoot(MY_MEDIA_ROOT_ID, null)

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        result.sendResult(currentPlaylist.map { it.toMediaItem() }.toMutableList())
    }
}

fun MediaMetadataCompat.toMusic() = Music(
    musicId = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID).toLong(),
    displayName = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE),
    title = getString(MediaMetadataCompat.METADATA_KEY_TITLE),
    artist = getString(MediaMetadataCompat.METADATA_KEY_ARTIST),
    album = getString(MediaMetadataCompat.METADATA_KEY_ALBUM),
    albumArtUri = getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI),
    genre = getString(MediaMetadataCompat.METADATA_KEY_GENRE),
    uri = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI),
    filePath = getString(METADATA_KEY_PATH),
    fileName = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).substringAfterLast('/')
)

fun Music.toMediaItem() =
    MediaBrowserCompat.MediaItem(
        createMediaMetadataCompat().description,
        MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
    )

fun Music.createMediaMetadataCompat(): MediaMetadataCompat = MediaMetadataCompat.Builder()
    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, musicId.toString())
    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, displayName)
    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
    .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre)
    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, albumArtUri)
    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, albumArtUri)
    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, uri)
    .putString(METADATA_KEY_PATH, filePath)
    .build()
