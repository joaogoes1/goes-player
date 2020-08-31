package com.goestech.goesplayer.data.datasource.music

import com.goestech.goesplayer.bussiness.model.MusicModel

interface MusicDataSource {

    fun getAllMusics(): List<MusicModel>
}