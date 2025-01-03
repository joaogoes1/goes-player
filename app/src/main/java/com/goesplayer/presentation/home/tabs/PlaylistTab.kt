package com.goesplayer.presentation.home.tabs

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.goesplayer.BancoController
import com.goesplayer.R
import com.goesplayer.data.model.Playlist
import com.goesplayer.presentation.home.HomeList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PlaylistTab(
    context: Context,
    playlistsLiveData: MutableLiveData<List<Playlist>>,
    isLoadingLiveData: MutableLiveData<Boolean>,
) {
    // TODO: Improve this after implement MVVM and ViewModel
    val isLoading = isLoadingLiveData.observeAsState()
    val playlists = playlistsLiveData.observeAsState()
    val shouldShowDeletePlaylistDialog = remember { mutableStateOf(false) }
    var deletePlaylistDialogPlaylistId: Long? = null

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading.value == true) {
            return@Box Box(
                Modifier.fillMaxSize(),
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        if (shouldShowDeletePlaylistDialog.value) {
            deletePlaylistDialogPlaylistId?.let {
                val successMessage = stringResource(R.string.delete_playlist_success_message)
                val errorMessage = stringResource(R.string.delete_playlist_error_message)
                DeletePlaylistDialog(
                    id = it ,
                    shouldShowDialog = shouldShowDeletePlaylistDialog,
                    confirmAction = { id ->
                        deletePlaylist(
                            id,
                            context,
                            playlistsLiveData,
                            isLoadingLiveData,
                            successMessage,
                            errorMessage,
                        )
                        shouldShowDeletePlaylistDialog.value = false
                    }
                )
            }
        }

        HomeList(
            onClick = { item ->
                // TODO: Implement this
            },
            onLongClick = { index ->
                deletePlaylistDialogPlaylistId = playlists.value?.get(index)?.id ?: -1
                shouldShowDeletePlaylistDialog.value = true
            },
            title = "Playlists",
            items = playlists.value?.map { it.name } ?: emptyList(),
        )
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun loadPlaylist(
    context: Context,
    playlists: MutableLiveData<List<Playlist>>,
    isLoading: MutableLiveData<Boolean>,
) {
    // TODO: Remove this after migrate to MVVM architecture
    GlobalScope.launch(Dispatchers.IO) {
        val crud = BancoController(context)
        val results = crud.loadPlaylists()
        withContext(Dispatchers.Main) {
            playlists.value = results
            isLoading.value = false
        }
    }
}


private fun deletePlaylist(
    id: Long,
    context: Context,
    playlists: MutableLiveData<List<Playlist>>,
    isLoading: MutableLiveData<Boolean>,
    successMessage: String,
    errorMessage: String,
) {
    val crud = BancoController(context)
    val result = crud.deletePlaylist(id)
    val toastText = if (result) {
        loadPlaylist(context, playlists, isLoading)
        successMessage
    } else {
        errorMessage
    }
    Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
}

@Composable
private fun CreatePlaylistDialog(
    name: String,
    shouldShowDialog: MutableState<Boolean>,
    dialogTextFieldValue: MutableState<String>,
) {
    AlertDialog(
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
                value = dialogTextFieldValue.value,
                onValueChange = { dialogTextFieldValue.value = it },
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                )
            )
        },
        onDismissRequest = { shouldShowDialog.value = false },
        dismissButton = {
            Button(onClick = {}) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            Button(onClick = {}) {
                Text(stringResource(R.string.confirm))
            }
        },
    )
}

@Composable
private fun DeletePlaylistDialog(
    id: Long,
    shouldShowDialog: MutableState<Boolean>,
    confirmAction: (Long) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { shouldShowDialog.value = false },
        containerColor = Color(0xFF151515),
        title = {
            Text(
                stringResource(R.string.delete_playlist_dialog_title),
            )
        },
        text = {
            Text(
                stringResource(R.string.delete_playlist_dialog_text, id),
            )
        },
        dismissButton = {
            Button(onClick = { shouldShowDialog.value = false }) {
                Text(
                    stringResource(R.string.cancel),
                    color = Color.Black,
                )
            }
        },
        confirmButton = {
            Button(onClick = { confirmAction(id) }) {
                Text(
                    stringResource(R.string.confirm),
                    color = Color.Black,
                )
            }
        },
    )
}
