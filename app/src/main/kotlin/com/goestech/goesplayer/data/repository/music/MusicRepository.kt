package com.goestech.goesplayer.data.repository.music

import com.goestech.goesplayer.data.Result
import com.goestech.goesplayer.data.datasource.music.SearchMusicError
import com.goestech.goesplayer.data.model.Music

interface MusicRepository {
    suspend fun loadMusicsFromDeviceStorage(): Result<Unit, SearchMusicError>
    suspend fun getMusic(musicId: Long): Music
    suspend fun getAllMusics(): List<Music>
    suspend fun getAllArtists(): List<String>
    suspend fun playMusic(selectedMusic: Music, playlist: List<Music>): Result<Unit, SearchMusicError>
}
