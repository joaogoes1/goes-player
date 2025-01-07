package com.goesplayer.data.model

data class Playlist(
    val id: Long,
    val name: String,
)

data class PlaylistWithMusics(
    val id: Long,
    val name: String,
    val musics: List<Music>
)
