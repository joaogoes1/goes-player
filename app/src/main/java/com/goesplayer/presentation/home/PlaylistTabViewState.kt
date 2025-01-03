package com.goesplayer.presentation.home

import com.goesplayer.data.model.Playlist

sealed class PlaylistTabViewState {
    data object Loading : PlaylistTabViewState()
    data object Error : PlaylistTabViewState()
    data class Success(
        val playlists: List<Playlist>
    ) : PlaylistTabViewState()
}