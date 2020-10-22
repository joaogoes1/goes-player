package com.goestech.goesplayer.view.player

import android.support.v4.media.MediaMetadataCompat

interface Player {

    fun playFromMedia(media: MediaMetadataCompat)

    fun pause()

    fun stop()

    fun seekTo(pos: Long)
}