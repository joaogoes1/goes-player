package com.goestech.goesplayer.data.repository.lyrics

import com.goestech.goesplayer.data.Result
import com.goestech.goesplayer.data.datasource.lyrics.LyricsError
import com.goestech.goesplayer.data.model.Lyrics

interface LyricsRepository {
    suspend fun getLyrics(music: String, artist: String): Result<Lyrics, LyricsError>
}