package com.goestech.goesplayer.view.home.music

import com.goestech.goesplayer.data.model.Music

interface MusicFragmentListener {
    fun playMusic(music: Music)
}