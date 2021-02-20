package com.goesplayer.lyrics.data.api

import com.goesplayer.lyrics.data.response.LyricsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LyricsApi {
    @GET("search.php")
    fun getLyrics(
        @Query("mus") musicName: String,
        @Query("art") artistName: String,
    ): Call<LyricsResponse>
}