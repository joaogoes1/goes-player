package com.goesplayer.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.goesplayer.presentation.MainActivityViewModel

@Composable
fun HomeRoute(
    navigateToPlayer: () -> Unit,
    activityViewModel: MainActivityViewModel,
    homeViewModel: HomeViewModel,
) {
    homeViewModel.loadPlaylists()
    val playlistViewState = homeViewModel.playlistTabViewState.observeAsState()

    HomeScreen(
        playSong = {
            // activityViewModel.playSong()
            navigateToPlayer()
        },
        deletePlaylistAction = homeViewModel::deletePlaylist,
        createPlaylistAction = homeViewModel::createPlaylist,
        loadPlaylistsRetryAction = homeViewModel::loadPlaylists,
        songList = activityViewModel.songList,
        isMusicActive = remember { mutableStateOf(false) },
        isMusicPlaying = remember { mutableStateOf(false) },
        addMusicToPlaylistAction = homeViewModel::addMusicToPlaylist,
        getPlaylistsAction = homeViewModel::getPlaylist,
        playlistTabViewState = playlistViewState.value ?: PlaylistTabViewState.Error,
    )
}