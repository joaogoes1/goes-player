package com.goestech.goesplayer.data.repository.music

import com.goestech.goesplayer.data.Result
import com.goestech.goesplayer.data.datasource.music.SearchMusicError
import com.goestech.goesplayer.data.entity.Music

interface MusicRepository {
    suspend fun loadMusicsFromDeviceStorage(): Result<Unit, SearchMusicError>
    suspend fun getAllMusics(): List<Music>
    suspend fun getAllArtists(): List<String>
}
