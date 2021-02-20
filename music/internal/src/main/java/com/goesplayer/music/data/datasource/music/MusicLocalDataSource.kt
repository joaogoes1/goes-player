package com.goesplayer.music.data.datasource.music

import com.goesplayer.commons.data.Result
import com.goesplayer.music.data.SearchMusicError
import com.goesplayer.music.data.model.Music

interface MusicLocalDataSource {

    suspend fun getAllMusics(): List<Music>

    suspend fun getMusic(musicId: Long): Result<Music, SearchMusicError>

    suspend fun saveMusics(musics: List<Music>)

    suspend fun getAllArtists(): List<String>

    suspend fun getAllAlbums(): List<String>

    suspend fun getAllFolders(): List<String>

    suspend fun getAllGenres(): List<String>
}