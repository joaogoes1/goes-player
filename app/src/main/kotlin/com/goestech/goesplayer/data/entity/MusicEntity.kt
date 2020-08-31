package com.goestech.goesplayer.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MusicEntity(
    val id: Long,
    val displayName: String,
    val title: String,
    val artist: String,
    val album: String,
    val genre: String,
    val filePath: String
)