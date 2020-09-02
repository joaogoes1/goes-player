package com.goestech.goesplayer.data.datasource.music

import com.goestech.goesplayer.bussiness.model.MusicModel
import com.goestech.goesplayer.data.entity.MusicEntity

class MusicLocalDataSource : MusicDataSource {
    override suspend fun getAllMusics(): List<MusicModel> {
        TODO("Not yet implemented")
    }

    override suspend fun saveMusics(musics: List<MusicEntity>) {

    }
}
