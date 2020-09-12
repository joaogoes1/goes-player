package com.goestech.goesplayer.data.datasource.music

import com.goestech.goesplayer.data.entity.Music

interface MusicLocalDataSource {

    suspend fun getAllMusics(): List<Music>

    suspend fun saveMusics(musics: List<Music>)
}