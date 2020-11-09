package com.goestech.goesplayer.player

import android.support.v4.media.MediaMetadataCompat

interface Player {

    fun playFromMedia(music: MediaMetadataCompat)

    fun pause()

    fun stop()

    fun seekTo(pos: Int)
}