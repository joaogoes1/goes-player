package com.goesplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.goesplayer.presentation.home.HomeList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CreatePlaylistDialog(
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
                Text(stringResource(R.string.confirme))
            }
        },
    )
}

@Composable
fun DeletePlaylistDialog(
    name: String,
    shouldShowDialog: MutableState<Boolean>,
    confirmAction: (String) -> Unit,
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
                stringResource(R.string.delete_playlist_dialog_text, name),
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
            Button(onClick = { confirmAction(name) }) {
                Text(
                    stringResource(R.string.confirme),
                    color = Color.Black,
                )
            }
        },
    )
}

class PlaylistFragment : Fragment() {
    private val playlists = MutableLiveData(emptyList<String>())
    private val isLoading = MutableLiveData(false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val isLoading by isLoading.observeAsState()
                val playlists by playlists.observeAsState()
                val shouldShowDeletePlaylistDialog = remember { mutableStateOf(false) }
                var deletePlaylistDialogPlaylistName = ""

                AppTheme {
                    if (isLoading == true)
                        return@AppTheme Box(
                            Modifier
                                .fillMaxWidth()
                                .fillMaxWidth()
                        ) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }

                    if (shouldShowDeletePlaylistDialog.value)
                        DeletePlaylistDialog(
                            name = deletePlaylistDialogPlaylistName,
                            shouldShowDialog = shouldShowDeletePlaylistDialog,
                            confirmAction = {
                                deletePlaylist(it, BancoController(requireContext()))
                                shouldShowDeletePlaylistDialog.value = false
                            }
                        )

                    HomeList(
                        onClick = { item ->
                            val intent = Intent(
                                context,
                                PlaylistResultActivity::class.java
                            )
                            intent.putExtra("nome", playlists?.get(item))
                            startActivity(intent)
                        },
                        onLongClick = { index ->
                            deletePlaylistDialogPlaylistName = playlists?.get(index) ?: ""
                            shouldShowDeletePlaylistDialog.value = true
                        },
                        title = "Playlists",
                        items = playlists ?: emptyList(),
                    )
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: Remove this after migrate to MVVM architecture
        GlobalScope.launch(Dispatchers.IO) {
            val crud = BancoController(requireContext())
            val cursor = crud.carregaPlaylist()
            val columnIndex = cursor.getColumnIndex(DataBaseOpenHelper.DB_NOME)
            if (columnIndex < 0) return@launch
            val results = mutableListOf<String>()
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                results.add(cursor.getString(columnIndex))
            }
            cursor.close()
            withContext(Dispatchers.Main) {
                playlists.value = results
                isLoading.value = false
            }
        }
    }

    private fun deletePlaylist(name: String, crud: BancoController) {
        val id = crud.acharPlaylist(name)
        val result = crud.deletePlaylist(id)
        val toastText = if (result) {
            getString(R.string.delete_playlist_success_message)
        } else {
            getString(R.string.delete_playlist_error_message)
        }
        Toast.makeText(requireContext(), toastText, Toast.LENGTH_LONG).show()
    }
}