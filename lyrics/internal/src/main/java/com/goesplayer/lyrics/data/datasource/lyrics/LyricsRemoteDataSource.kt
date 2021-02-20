package com.goesplayer.lyrics.data.datasource.lyrics

import com.goesplayer.commons.data.Result
import com.goestech.lyrics.data.LyricsError
import com.goestech.lyrics.data.model.Lyrics

interface LyricsRemoteDataSource {
    suspend fun getLyrics(music: String, artist: String): Result<Lyrics, LyricsError>
}
