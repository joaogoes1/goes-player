package com.goesplayer.presentation.musiclist

import androidx.compose.runtime.Composable
import com.goesplayer.data.model.Music
import kotlinx.serialization.Serializable

@Serializable
sealed class SearchProperties
@Serializable
data class Artist(val name: String) : SearchProperties()
@Serializable
data class Album(val artist: String, val albumName: String) : SearchProperties()
@Serializable
data class Genre(val name: String) : SearchProperties()
@Serializable
data class Folder(val name: String) : SearchProperties()


@Composable
fun MusicListRoute(
    title: String,
    musicList: List<Music>,
    searchProperties: SearchProperties
) {
    val filteredMusics = musicList
        .filter {
            when (searchProperties) {
                is Artist -> {
                    it.artist == searchProperties.name
                }
                is Album -> {
                    it.artist == searchProperties.artist && it.album == searchProperties.albumName
                }
                is Genre -> {
                    it.genre == searchProperties.name
                }
                is Folder -> {
                    it.folder == searchProperties.name
                }
            }
        }

    MusicListScreen(
        title = title,
        songList = filteredMusics
    )
}