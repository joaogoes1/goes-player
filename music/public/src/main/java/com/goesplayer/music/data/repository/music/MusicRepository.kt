package com.goesplayer.music.data.repository.music

import com.goesplayer.music.data.model.Music
import com.goesplayer.commons.data.Result
import com.goesplayer.music.data.SearchMusicError

interface MusicRepository {
    suspend fun loadMusicsFromDeviceStorage(): Result<Unit, SearchMusicError>
    suspend fun getMusic(musicId: Long): Result<Music, SearchMusicError>
    suspend fun getAllMusics(): List<Music>
    suspend fun getAllArtists(): List<String>
    suspend fun getAllAlbums(): List<String>
    suspend fun getAllFolders(): List<String>
    suspend fun getAllGenres(): List<String>
    suspend fun playMusic(selectedMusic: Music, playlist: List<Music>): Result<Unit, SearchMusicError>
}