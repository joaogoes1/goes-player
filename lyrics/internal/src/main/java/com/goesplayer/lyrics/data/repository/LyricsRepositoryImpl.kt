package com.goesplayer.lyrics.data.repository

import com.goesplayer.commons.data.Result
import com.goesplayer.lyrics.data.datasource.lyrics.LyricsRemoteDataSource
import com.goestech.lyrics.data.LyricsError
import com.goestech.lyrics.data.model.Lyrics

class LyricsRepositoryImpl(
    private val lyricsRemoteDataSource: LyricsRemoteDataSource
) : com.goestech.lyrics.data.repository.LyricsRepository {
    override suspend fun getLyrics(music: String, artist: String): Result<Lyrics, LyricsError> =
        lyricsRemoteDataSource.getLyrics(music, artist)
}
