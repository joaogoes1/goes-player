package com.goesplayer.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.goesplayer.data.model.Playlist
import com.goesplayer.presentation.MainActivityViewModel
import com.goesplayer.presentation.MusicListRouteConfig
import com.goesplayer.presentation.PlayerViewState
import com.goesplayer.presentation.musiclist.MusicListScreen

@Composable
fun HomeRoute(
    navigateToPlayer: () -> Unit,
    navigateToMusicList: (MusicListRouteConfig) -> Unit,
    navigateToPlaylistDetails: (Long, String) -> Unit,
    activityViewModel: MainActivityViewModel,
    homeViewModel: HomeViewModel,
) {
    homeViewModel.loadPlaylists()
    val playlistViewState = homeViewModel
        .playlistTabViewState
        .observeAsState(PlaylistTabViewState.Loading)

    HomeScreen(
        playSong = { music ->
            activityViewModel.playSong(music)
            navigateToPlayer()
        },
        deletePlaylistAction = homeViewModel::deletePlaylist,
        createPlaylistAction = homeViewModel::createPlaylist,
        showPlaylistDetailsAction = { playlist ->
            navigateToPlaylistDetails(playlist.id, playlist.name)
        },
        loadPlaylistsRetryAction = homeViewModel::loadPlaylists,
        skipToPreviousAction = activityViewModel::skipToPrevious,
        playOrPauseAction = activityViewModel::playOrPause,
        skipToNextAction = activityViewModel::skipToNext,
        navigateToMusicList = navigateToMusicList,
        navigateToPlayer = navigateToPlayer,
        songList = activityViewModel.songList,
        playerViewState = activityViewModel.playerViewState.observeAsState(PlayerViewState.Loading),
        addMusicToPlaylistAction = homeViewModel::addMusicToPlaylist,
        getPlaylistsAction = homeViewModel::getPlaylist,
        playlistTabViewState = playlistViewState,
    )
}
