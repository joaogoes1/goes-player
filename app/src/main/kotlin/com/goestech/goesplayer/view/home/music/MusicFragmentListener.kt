package com.goestech.goesplayer.view.home.music

import com.goestech.goesplayer.data.entity.Music

interface MusicFragmentListener {
    fun playMusic(music: Music)
}