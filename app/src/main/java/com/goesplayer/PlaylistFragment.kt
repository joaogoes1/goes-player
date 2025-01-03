package com.goesplayer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


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
                    stringResource(R.string.confirm),
                    color = Color.Black,
                )
            }
        },
    )
}
