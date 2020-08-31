package com.goestech.goesplayer.bussiness.model

data class MusicModel(
    val id: Long,
    val displayName: String,
    val title: String,
    val artist: String,
    val album: String,
    val genre: String,
    val folder: String,
    val fileName: String,
    val completePath: String
)