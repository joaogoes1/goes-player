package com.goesplayer.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.goesplayer.presentation.MainActivityViewModel
import com.goesplayer.presentation.MusicListRouteConfig

@Composable
fun HomeRoute(
    navigateToPlayer: () -> Unit,
    navigateToMusicList: (MusicListRouteConfig) -> Unit,
    activityViewModel: MainActivityViewModel,
    homeViewModel: HomeViewModel,
) {
    homeViewModel.loadPlaylists()
    val playlistViewState = homeViewModel.playlistTabViewState.observeAsState()

    HomeScreen(
        playSong = { music ->
            activityViewModel.playSong(music)
            navigateToPlayer()
        },
        deletePlaylistAction = homeViewModel::deletePlaylist,
        createPlaylistAction = homeViewModel::createPlaylist,
        loadPlaylistsRetryAction = homeViewModel::loadPlaylists,
        navigateToMusicList = navigateToMusicList,
        songList = activityViewModel.songList,
        isMusicActive = remember { mutableStateOf(false) },
        isMusicPlaying = remember { mutableStateOf(false) },
        addMusicToPlaylistAction = homeViewModel::addMusicToPlaylist,
        getPlaylistsAction = homeViewModel::getPlaylist,
        playlistTabViewState = playlistViewState.value ?: PlaylistTabViewState.Success(emptyList()),
    )
}