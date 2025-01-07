package com.goesplayer.presentation.playlistdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.rememberNavController
import com.goesplayer.data.model.Playlist
import com.goesplayer.presentation.MainActivityViewModel
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistDetailsRouteConfig(
    val playlistId: Long,
    val name: String,
)

@Composable
fun PlaylistDetailsRoute(
    navigateToPlayer: () -> Unit,
    activityViewModel: MainActivityViewModel,
    viewModel: PlaylistDetailsViewModel,
    arguments: PlaylistDetailsRouteConfig,
) {
    viewModel.loadDetails(arguments.playlistId)
    PlaylistDetailsScreen(
        onClickAction = { list ->
            activityViewModel.playMusicList(list)
            navigateToPlayer()
        },
        retryAction = { viewModel.loadDetails(arguments.playlistId) },
        title = arguments.name,
        state = viewModel.viewState.observeAsState(PlaylistDetailsViewState.Loading),
        navController = rememberNavController(),
    )
}