package com.goesplayer.music.data.model

data class Music(
    val musicId: Long,
    val displayName: String?,
    val title: String?,
    val artist: String?,
    val album: String?,
    val albumArtUri: String?,
    val genre: String?,
    val duration: Long,
    val uri: String,
    val filePath: String,
    val fileName: String
)
