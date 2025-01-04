package com.goesplayer.presentation.musiclist

import androidx.compose.runtime.Composable
import com.goesplayer.data.model.Music
import com.goesplayer.presentation.MainActivityViewModel
import com.goesplayer.presentation.MusicListRouteConfig

@Composable
fun MusicListRoute(
    navigateToPlayer: () -> Unit,
    activityViewModel: MainActivityViewModel,
    title: String,
    musicList: List<Music>,
    searchProperties: MusicListRouteConfig
) {
    val filteredMusics = musicList
        .filter {
            var result = true
            if (searchProperties.artist != null) result =
                result && it.artist == searchProperties.artist
            if (searchProperties.album != null) result =
                result && it.artist == searchProperties.artist && it.album == searchProperties.album
            if (searchProperties.genre != null) result =
                result && it.genre == searchProperties.genre
            if (searchProperties.folder != null) result =
                result && it.folder == searchProperties.folder
            result
        }

    MusicListScreen(
        onClickAction = { list ->
            activityViewModel.playMusicList(list)
            navigateToPlayer()
        },
        title = title,
        songList = filteredMusics
    )
}