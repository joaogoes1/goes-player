package com.goestech.goesplayer.view.home.music

import com.goestech.goesplayer.bussiness.model.MusicModel

interface MusicFragmentListener {
    fun playMusic(music: MusicModel)
}