package com.goestech.goesplayer.data.api

import com.goestech.goesplayer.data.model.Lyrics
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LyricsApi {
    @GET("search.php")
    fun getLyrics(
        @Query("mus") musicName: String,
        @Query("art") artistName: String,
    ): Call<Lyrics>
}