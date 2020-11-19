package com.goestech.goesplayer.data.repository.lyrics

import com.goestech.goesplayer.data.Result
import com.goestech.goesplayer.data.datasource.lyrics.LyricsError
import com.goestech.goesplayer.data.datasource.lyrics.LyricsRemoteDataSource
import com.goestech.goesplayer.data.model.Lyrics

class LyricsRepositoryImpl(
    private val lyricsRemoteDataSource: LyricsRemoteDataSource
) : LyricsRepository {
    override suspend fun getLyrics(music: String, artist: String): Result<Lyrics, LyricsError> =
        lyricsRemoteDataSource.getLyrics(music, artist)
}
