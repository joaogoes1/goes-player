package com.goestech.goesplayer.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Lyrics(
    val type: String,
    @field:Json(name = "art") val artist: Artist?,
    @field:Json(name = "mus") val music: Music?
) {
    @JsonClass(generateAdapter = true)
    data class Artist(
        val id: String?,
        val name: String?,
        val url: String?
    )

    @JsonClass(generateAdapter = true)
    data class Music(
        val id: String?,
        val name: String?,
        val url: String?,
        val lang: Int?,
        val text: String?
    )
}