package com.goesplayer.presentation.home.tabs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.goesplayer.AppTheme
import com.goesplayer.BancoController
import com.goesplayer.MainActivity
import com.goesplayer.R
import com.goesplayer.data.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private data class MusicView(
    val id: Long,
    val name: String,
    val artist: String,
    val uri: Uri,
    val thumb: Bitmap?,
)

@Composable
fun MusicTab(
    activity: MainActivity,
    db: BancoController,
    context: Context,
) {
    val shouldShowMusicOptionsDialog = remember { mutableStateOf(false) }
    val shouldShowAddMusicToPlaylistDialog = remember { mutableStateOf(false) }
    var selectedMusicId: Long? = null
    var playlists = emptyList<Playlist>()

    Box(modifier = Modifier.fillMaxSize()) {
        MusicList(
            title = stringResource(R.string.music_tab_title),
            items = mapMusics(),
            onClick = { position ->
                activity.musicSrv.PlayList(MainActivity.todasMusicas)
                activity.musicSrv.posicao = position
                activity.musicSrv.reproduzir()
                activity.musicSrv.abrirPlayerTela()
            },
            onLongClick = { musicId ->
                shouldShowMusicOptionsDialog.value = true
                selectedMusicId = musicId
            }
        )

        if (shouldShowMusicOptionsDialog.value && selectedMusicId != null)
            MusicOptionsDialog(
                shouldShowMusicOptionsDialog,
            ) {
                // TODO: Remove this GlobalScope after migration to MVVM and ViewModel
                GlobalScope.launch(Dispatchers.IO) {
                    playlists = db.loadPlaylists()
                    withContext(Dispatchers.Main) {
                        shouldShowMusicOptionsDialog.value = false
                        shouldShowAddMusicToPlaylistDialog.value = true
                    }
                }
            }

        if (shouldShowAddMusicToPlaylistDialog.value)
            selectedMusicId?.let { musicId ->
                val successMessage = stringResource(R.string.add_music_to_playlist_success_message)
                val errorMessage = stringResource(R.string.add_music_to_playlist_error_message)
                AddMusicToPlaylistDialog(
                    onDismissRequest = {
                        shouldShowAddMusicToPlaylistDialog.value = false
                    },
                    onConfirm = { playlistName ->
                        addMusicToPlaylist(
                            db,
                            playlistName,
                            musicId,
                            context,
                            successMessage,
                            errorMessage,
                        )
                        shouldShowAddMusicToPlaylistDialog.value = false
                    },
                    items = playlists,
                )
            }
    }
}

private fun addMusicToPlaylist(
    db: BancoController,
    playlist: Playlist, musicId: Long,
    context: Context,
    successMessage: String,
    errorMessage: String,
) {
    val result = db.addToPlaylist(playlist.id, musicId)
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

private fun mapMusics() =
    MainActivity
        .todasMusicas
        .map {
            MusicView(
                it.idNumber,
                it.name,
                it.artist,
                it.uri,
                null
            )
        }
        .sortedBy { it.name }

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MusicList(
    onClick: (Int) -> Unit = {},
    onLongClick: (Long) -> Unit = {},
    title: String,
    items: List<MusicView>,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
            itemsIndexed(items) { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {
                                onClick(index)
                            },
                            onLongClick = {
                                onLongClick(item.id)
                            },
                        )
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        val retriever = MediaMetadataRetriever()
                        retriever.setDataSource(LocalContext.current, item.uri)
                        val imgBytes = retriever.embeddedPicture
                        if (imgBytes != null) {
                            val thumb =
                                BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size)
                            Image(
                                bitmap = thumb.asImageBitmap(),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(48.dp)
                            )
                        } else {
                            Image(painterResource(R.mipmap.teste_album), "")
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 8.dp),
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(
                                text = item.name,
                                maxLines = 1,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                text = item.artist,
                                maxLines = 1,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
                if (index < items.lastIndex) {
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider(color = Color.DarkGray, modifier = Modifier.width(64.dp))
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}


@Composable
private fun MusicOptionsDialog(
    shouldShowDialog: MutableState<Boolean>,
    showAddToPlaylistDialog: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { shouldShowDialog.value = false },
        containerColor = Color(0xFF151515),
        title = { Text(stringResource(R.string.music_options_dialog_title)) },
        text = {
            TextButton(
                onClick = showAddToPlaylistDialog,
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
        },
        confirmButton = {
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
    )
}