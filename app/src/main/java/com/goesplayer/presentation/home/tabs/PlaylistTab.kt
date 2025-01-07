package com.goesplayer.presentation.home.tabs

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.goesplayer.R
import com.goesplayer.presentation.components.ErrorScreen
import com.goesplayer.presentation.components.LoadingScreen
import com.goesplayer.presentation.components.SingleTextList
import com.goesplayer.presentation.home.PlaylistTabViewState

sealed class PlaylistTabDialogState {
    data object None : PlaylistTabDialogState()
    data object CreatePlaylist : PlaylistTabDialogState()
    data class DeletePlaylist(val id: Long, val name: String) : PlaylistTabDialogState()
}

@Composable
fun PlaylistTab(
    deletePlaylistAction: (Long) -> Boolean,
    createPlaylistAction: (String) -> Unit,
    showDeletePlaylistDialog: (Long, String) -> Unit,
    onDismissRequest: () -> Unit,
    retryAction: () -> Unit,
    playlistTabViewState: State<PlaylistTabViewState>,
    playlistTabDialogState: PlaylistTabDialogState,
) {
    val context = LocalContext.current

    when (playlistTabDialogState) {
        is PlaylistTabDialogState.None -> {}
        is PlaylistTabDialogState.CreatePlaylist -> {
            CreatePlaylistDialog(
                onDismissRequest = onDismissRequest,
                createPlaylistAction = createPlaylistAction
            )
        }

        is PlaylistTabDialogState.DeletePlaylist -> {
            val successMessage = stringResource(R.string.delete_playlist_success_message)
            val errorMessage = stringResource(R.string.delete_playlist_error_message)
            DeletePlaylistDialog(
                onDismissRequest = onDismissRequest,
                deletePlaylistAction = { id ->
                    deletePlaylist(
                        deletePlaylistAction,
                        id,
                        context,
                        successMessage,
                        errorMessage
                    )
                    onDismissRequest()
                },
                id = playlistTabDialogState.id,
                name = playlistTabDialogState.name,
            )
        }
    }

    when (val currentState = playlistTabViewState.value) {
        is PlaylistTabViewState.Loading -> {
            LoadingScreen()
        }

        is PlaylistTabViewState.Error -> {
            ErrorScreen(retryAction = retryAction)
        }

        is PlaylistTabViewState.Success -> {
            SingleTextList(
                onClick = { item ->
                    // TODO: Implement this
                },
                onLongClick = { index ->
                    val playlist = currentState.playlists[index]
                    showDeletePlaylistDialog(playlist.id, playlist.name)
                },
                title = stringResource(R.string.playlists_tab_title),
                items = currentState.playlists.map { it.name },
                emptyStateMessage = stringResource(R.string.playlists_tab_empty_state_message)
            )
        }
    }
}


private fun deletePlaylist(
    deletePlaylistAction: (Long) -> Boolean,
    id: Long,
    context: Context,
    successMessage: String,
    errorMessage: String,
) {
    val result = deletePlaylistAction(id)
    val toastText = if (result) {
        successMessage
    } else {
        errorMessage
    }
    Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
}

@Composable
private fun CreatePlaylistDialog(
    onDismissRequest: () -> Unit,
    createPlaylistAction: (String) -> Unit,
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = Color(0xFF151515),
        title = {
            Text(
                stringResource(R.string.playlist_tab_create_playlist_dialog_title),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                )
            )
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(
                    stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                createPlaylistAction(text.text)
                onDismissRequest()
            }) {
                Text(
                    stringResource(R.string.confirm),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
    )
}

@Composable
private fun DeletePlaylistDialog(
    onDismissRequest: () -> Unit,
    deletePlaylistAction: (Long) -> Unit,
    name: String,
    id: Long,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = Color(0xFF151515),
        title = {
            Text(
                stringResource(R.string.delete_playlist_dialog_title),
            )
        },
        text = {
            Text(
                stringResource(R.string.delete_playlist_dialog_text, name),
            )
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(
                    stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
        confirmButton = {
            Button(onClick = { deletePlaylistAction(id) }) {
                Text(
                    stringResource(R.string.confirm),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
    )
}
