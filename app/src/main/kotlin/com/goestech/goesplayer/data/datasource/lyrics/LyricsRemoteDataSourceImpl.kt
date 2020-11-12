package com.goestech.goesplayer.data.datasource.lyrics

import com.goestech.goesplayer.data.Result
import com.goestech.goesplayer.data.api.LyricsApi
import com.goestech.goesplayer.data.model.Lyrics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class LyricsRemoteDataSourceImpl(
    private val lyricsApi: LyricsApi
) : LyricsRemoteDataSource {
    override suspend fun getLyrics(music: String, artist: String): Result<Lyrics, LyricsError> = withContext(Dispatchers.IO) {
        val response = lyricsApi.getLyrics(music, artist).awaitResponse().body()
        return@withContext if (response != null) {
            Result.Success(response)
        } else {
            Result.Error(LyricsError())
        }
    }
}