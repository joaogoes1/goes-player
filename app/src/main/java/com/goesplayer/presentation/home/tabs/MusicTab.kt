package com.goesplayer.presentation.home.tabs

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.goesplayer.R
import com.goesplayer.data.model.Music
import com.goesplayer.data.model.Playlist
import com.goesplayer.presentation.components.DoubleTextWithAlbumArtList
import com.goesplayer.presentation.components.DoubleTextWithAlbumItemView
import com.goesplayer.presentation.home.SearchPlaylistsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


sealed class MusicTabDialogState {
    data object None : MusicTabDialogState()
    data object Loading : MusicTabDialogState()
    data class Error(val errorMessage: String) : MusicTabDialogState()
    data class Options(val music: Music) : MusicTabDialogState()
    data class AddToPlaylist(
        val music: Music,
        val playlists: List<Playlist>,
    ) : MusicTabDialogState()
}

@Composable
fun MusicTab(
    playSong: (Music) -> Unit,
    addMusicToPlaylistAction: (Music, Playlist) -> Boolean,
    getPlaylistsAction: () -> Flow<SearchPlaylistsState>,
    songList: List<Music>,
) {
    val musicTabDialogState =
        remember { mutableStateOf<MusicTabDialogState>(MusicTabDialogState.None) }
    val context = LocalContext.current
    val coroutineContext = rememberCoroutineScope()

    when (val currentDialogState = musicTabDialogState.value) {
        is MusicTabDialogState.None -> {}
        is MusicTabDialogState.Loading -> {
            AlertDialog(
                onDismissRequest = { musicTabDialogState.value = MusicTabDialogState.None },
                containerColor = Color(0xFF151515),
                text = {
                    CircularProgressIndicator()
                },
                dismissButton = {},
                confirmButton = {}
            )
        }

        is MusicTabDialogState.Error -> {
            AlertDialog(
                onDismissRequest = { musicTabDialogState.value = MusicTabDialogState.None },
                containerColor = Color(0xFF151515),
                text = {
                    CircularProgressIndicator()
                },
                confirmButton = {
                    Button(
                        onClick = { musicTabDialogState.value = MusicTabDialogState.None },
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            )
            Dialog(onDismissRequest = { musicTabDialogState.value = MusicTabDialogState.None }) {
                Text(currentDialogState.errorMessage)
            }
        }

        is MusicTabDialogState.Options -> {
            MusicOptionsDialog(
                { music ->
                    coroutineContext.launch {
                        getPlaylistsAction().collect {
                            when (it) {
                                is SearchPlaylistsState.Loading -> {
                                    musicTabDialogState.value = MusicTabDialogState.Loading
                                }

                                is SearchPlaylistsState.Error -> {
                                    musicTabDialogState.value = MusicTabDialogState.Error(
                                        "Erro ao carregar playlists"
                                    )
                                }

                                is SearchPlaylistsState.Success -> {
                                    musicTabDialogState.value = MusicTabDialogState.AddToPlaylist(
                                        playlists = it.list,
                                        music = music,
                                    )
                                }
                            }
                        }
                    }
                },
                { musicTabDialogState.value = MusicTabDialogState.None },
                (musicTabDialogState.value as MusicTabDialogState.Options).music // TODO: Find better approach
            )
        }

        is MusicTabDialogState.AddToPlaylist -> {
            val successMessage = stringResource(R.string.add_music_to_playlist_success_message)
            val errorMessage = stringResource(R.string.add_music_to_playlist_error_message)
            val music = currentDialogState.music
            AddMusicToPlaylistDialog(
                onDismissRequest = {
                    musicTabDialogState.value = MusicTabDialogState.None
                },
                onConfirm = { playlist ->
                    addMusicToPlaylistResult(
                        addMusicToPlaylistAction(music, playlist),
                        context,
                        successMessage,
                        errorMessage
                    )
                    musicTabDialogState.value = MusicTabDialogState.None
                },
                items = currentDialogState.playlists,
            )
        }
    }
    DoubleTextWithAlbumArtList(
        title = stringResource(R.string.music_tab_title),
        items = mapMusics(songList),
        onClick = { position ->
            playSong(songList[position])
        },
        emptyStateMessage = stringResource(R.string.music_tab_empty_state_message),
        onLongClick = { position ->
            musicTabDialogState.value = MusicTabDialogState.Options(songList[position])
        }
    )
}

private fun addMusicToPlaylistResult(
    result: Boolean,
    context: Context,
    successMessage: String,
    errorMessage: String,
) {
    if (result) {
        Toast.makeText(
            context,
            successMessage, Toast.LENGTH_LONG
        ).show()
    } else {
        Toast.makeText(
            context,
            errorMessage, Toast.LENGTH_LONG
        ).show()
    }
}

private fun mapMusics(songList: List<Music>) =
    songList
        .map {
            DoubleTextWithAlbumItemView(
                it.id,
                it.title,
                it.artist,
                it.songUri,
                null
            )
        }
        .sortedBy { it.name }

@Composable
private fun MusicOptionsDialog(
    showAddToPlaylistDialog: (Music) -> Unit,
    onDismissRequest: () -> Unit,
    music: Music
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = Color(0xFF151515),
        title = { Text(stringResource(R.string.music_options_dialog_title)) },
        text = {
            TextButton(
                onClick = { showAddToPlaylistDialog(music) },
                shape = RectangleShape,
            ) {
                Text(
                    text = stringResource(R.string.music_tab_add_to_playlist_dialog_option),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {}
    )
}

@Composable
private fun AddMusicToPlaylistDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (Playlist) -> Unit,
    items: List<Playlist>,
) {
    val playlistSelected = remember { mutableStateOf<Playlist?>(null) }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = Color(0xFF151515),
        title = { Text(stringResource(R.string.music_tab_add_to_playlist_dialog_title)) },
        text = {
            if (items.isEmpty()) {
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.music_tab_add_to_playlist_dialog_empty_state_title),
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.LightGray),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(R.string.music_tab_add_to_playlist_dialog_empty_state_description),
                        style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                ) {
                    items(items) { item ->
                        Box(
                            modifier = Modifier
                                .clickable { playlistSelected.value = item }
                                .background(color = if (item == playlistSelected.value) Color.DarkGray else Color.Transparent)
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                        ) {
                            Text(
                                item.name,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }

                    }
                }
            }
        },
        confirmButton = {
            if (items.isNotEmpty()) {
                Button(
                    onClick = {
                        playlistSelected.value?.let { onConfirm(it) }
                    }
                ) {
                    Text(
                        stringResource(R.string.confirm),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    )
}