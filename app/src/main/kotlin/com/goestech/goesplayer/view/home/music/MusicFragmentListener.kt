package com.goesplayer.view.home.music

import com.goesplayer.music.data.model.Music

interface MusicFragmentListener {
    fun playMusic(music: Music)
}