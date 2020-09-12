package com.goestech.goesplayer.data.datasource.music

import com.goestech.goesplayer.data.database.AppDatabase
import com.goestech.goesplayer.data.entity.Music
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MusicLocalDataSourceImpl(
    private val database: AppDatabase
) : MusicLocalDataSource {
    override suspend fun getAllMusics(): List<Music> = withContext(Dispatchers.IO) {
        database.musicDao().getAllMusics()
    }

    override suspend fun saveMusics(musics: List<Music>) = withContext(Dispatchers.IO) {
        database.musicDao().insertAll(*musics.toTypedArray())
    }
}
