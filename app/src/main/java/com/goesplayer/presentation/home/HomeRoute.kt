package com.goesplayer.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.goesplayer.presentation.MainActivityViewModel

@Composable
fun HomeRoute(
    navigateToPlayer: () -> Unit,
    activityViewModel: MainActivityViewModel,
    homeViewModel: HomeViewModel,
) {
    HomeScreen(
        playSong = {
            // activityViewModel.playSong()
            navigateToPlayer()
        },
        songList = activityViewModel.songList,
        isMusicActive = remember { mutableStateOf(false) },
        isMusicPlaying = remember { mutableStateOf(false) },
        playlistsLiveData = activityViewModel.playlists,
        isLoadingLiveData = activityViewModel.isLoadingPlaylists
    )
}