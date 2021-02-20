package com.goestech.lyrics.data.repository

import com.goesplayer.commons.data.Result
import com.goestech.lyrics.data.LyricsError
import com.goestech.lyrics.data.model.Lyrics

interface LyricsRepository {
    suspend fun getLyrics(music: String, artist: String): Result<Lyrics, LyricsError>
}