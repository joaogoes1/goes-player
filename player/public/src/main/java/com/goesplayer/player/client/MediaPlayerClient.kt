package com.goesplayer.player.client

import com.goesplayer.music.data.model.Music

interface MediaPlayerClient {
    val isPlaying: Boolean
    val currentMusic: Music?
}