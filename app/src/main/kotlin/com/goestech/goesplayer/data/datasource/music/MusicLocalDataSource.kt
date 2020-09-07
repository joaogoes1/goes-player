package com.goestech.goesplayer.data.datasource.music

import com.goestech.goesplayer.bussiness.model.MusicModel
import com.goestech.goesplayer.data.entity.MusicEntity

interface MusicLocalDataSource {

    suspend fun getAllMusics(): List<MusicModel>

    suspend fun saveMusics(musics: List<MusicEntity>)
}