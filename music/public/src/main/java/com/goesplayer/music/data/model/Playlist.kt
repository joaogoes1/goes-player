package com.goesplayer.music.data.model

data class Playlist(
    val playlistId: Long,
    val name: String,
    val musics: List<Music>,
)