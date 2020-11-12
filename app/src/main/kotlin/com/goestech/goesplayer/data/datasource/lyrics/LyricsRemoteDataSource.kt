package com.goestech.goesplayer.data.datasource.lyrics

import com.goestech.goesplayer.data.Result
import com.goestech.goesplayer.data.model.Lyrics

interface LyricsRemoteDataSource {
    suspend fun getLyrics(music: String, artist: String): Result<Lyrics, LyricsError>
}

class LyricsError {

}
