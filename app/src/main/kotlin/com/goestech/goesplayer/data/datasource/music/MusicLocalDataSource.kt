package com.goestech.goesplayer.data.datasource.music

import com.goestech.goesplayer.data.model.Music

interface MusicLocalDataSource {

    suspend fun getAllMusics(): List<Music>

    suspend fun getMusic(musicId: Long): Music

    suspend fun saveMusics(musics: List<Music>)

    suspend fun getAllArtists(): List<String>

    suspend fun getAllAlbums(): List<String>

    suspend fun getAllFolders(): List<String>

    suspend fun getAllGenres(): List<String>
}