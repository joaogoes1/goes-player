package com.goesplayer.presentation.playlistdetails

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.goesplayer.R
import com.goesplayer.data.model.Music
import com.goesplayer.presentation.components.BackButton
import com.goesplayer.presentation.components.DoubleTextWithAlbumArtList
import com.goesplayer.presentation.components.DoubleTextWithAlbumItemView
import com.goesplayer.presentation.components.EmptyScreen
import com.goesplayer.presentation.components.ErrorScreen
import com.goesplayer.presentation.components.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailsScreen(
    onClickAction: (List<Music>) -> Unit,
    retryAction: () -> Unit,
    title: String,
    state: State<PlaylistDetailsViewState>,
    navController: NavController,
) {
    when (val currentState = state.value) {
        is PlaylistDetailsViewState.Error -> {
            ErrorScreen(retryAction)
        }

        is PlaylistDetailsViewState.Empty -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(title) },
                        colors = TopAppBarDefaults.topAppBarColors().copy(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                        navigationIcon = { BackButton(navController) }
                    )
                }
            ) {
                EmptyScreen(
                    modifier = Modifier
                        .padding(it)
                        .padding(24.dp),
                    text = stringResource(R.string.playlist_details_screen_empty_state_message)
                )
            }
        }

        is PlaylistDetailsViewState.Loading -> {
            LoadingScreen()
        }

        is PlaylistDetailsViewState.Success -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(title) },
                        colors = TopAppBarDefaults.topAppBarColors().copy(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                        navigationIcon = { BackButton(navController) }
                    )
                }
            ) { innerPadding ->
                DoubleTextWithAlbumArtList(
                    modifier = Modifier.padding(innerPadding),
                    onClick = { onClickAction(currentState.musicList) },
                    onLongClick = {},
                    title = "Músicas",
                    emptyStateMessage = "",
                    items = currentState.musicList.map {
                        DoubleTextWithAlbumItemView(
                            it.id,
                            it.title,
                            it.artist,
                            it.albumArtUri ?: Uri.EMPTY,
                            thumb = null,
                        )
                    },
                )
            }
        }
    }
}