package com.goesplayer.player.mapper

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.goesplayer.music.data.model.Music
import com.goesplayer.player.service.METADATA_KEY_PATH

fun MediaMetadataCompat.toMusic() = Music(
    musicId = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID).toLong(),
    displayName = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE),
    title = getString(MediaMetadataCompat.METADATA_KEY_TITLE),
    artist = getString(MediaMetadataCompat.METADATA_KEY_ARTIST),
    album = getString(MediaMetadataCompat.METADATA_KEY_ALBUM),
    albumArtUri = getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI),
    genre = getString(MediaMetadataCompat.METADATA_KEY_GENRE),
    duration = getLong(MediaMetadataCompat.METADATA_KEY_DURATION),
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
    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, uri)
    .putString(METADATA_KEY_PATH, filePath)
    .build()
