package com.goesplayer.lyrics.data.datasource.lyrics

import android.util.Log
import com.goesplayer.commons.data.Result
import com.goesplayer.lyrics.data.api.LyricsApi
import com.goesplayer.lyrics.data.response.LyricsResponse
import com.goestech.lyrics.data.LyricsError
import com.goestech.lyrics.data.model.Lyrics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class LyricsRemoteDataSourceImpl(
    private val lyricsApi: LyricsApi
) : LyricsRemoteDataSource {

    override suspend fun getLyrics(music: String, artist: String): Result<Lyrics, LyricsError> =
        withContext(Dispatchers.IO) {
            val response = try {
                lyricsApi.getLyrics(music, artist).awaitResponse().body()
            } catch (e: Exception) {
                Log.e("Load lyrics error: ", e.localizedMessage ?: "No message")
                null
            }
            return@withContext if (response != null) {
                Result.Success(
                    response.toModel()
                )
            } else {
                Result.Error(LyricsError.UnknownError)
            }
        }
}

fun LyricsResponse.toModel() = Lyrics(music?.get(0)?.text)