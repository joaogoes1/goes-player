package com.goestech.goesplayer.data.datasource.music

import com.goestech.goesplayer.data.database.dao.MusicDao
import com.goestech.goesplayer.data.model.Music
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MusicLocalDataSourceImpl(
    private val musicDao: MusicDao
) : MusicLocalDataSource {
    override suspend fun getAllMusics(): List<Music> = withContext(Dispatchers.IO) {
        musicDao.getAllMusics()
    }

    override suspend fun getMusic(musicId: Long): Music = withContext(Dispatchers.IO) {
        musicDao.getMusic(musicId)
    }

    override suspend fun saveMusics(musics: List<Music>) = withContext(Dispatchers.IO) {
        musicDao.insertAll(*musics.toTypedArray())
    }

    override suspend fun getAllArtists(): List<String> = withContext(Dispatchers.IO) {
        musicDao.getAllArtists()
    }

    override suspend fun getAllAlbums(): List<String> = withContext(Dispatchers.IO) {
        musicDao.getAllAlbums().mapNotNull { it }
    }

    override suspend fun getAllFolders(): List<String> = withContext(Dispatchers.IO) {
        // TODO: Fix this
        musicDao.getAllFolders().mapNotNull {
            it?.substringBeforeLast("/")
            it?.substringAfterLast("/")
        }
    }

    override suspend fun getAllGenres(): List<String> = withContext(Dispatchers.IO) {
        musicDao.getAllGenres().mapNotNull { it }
    }
}
