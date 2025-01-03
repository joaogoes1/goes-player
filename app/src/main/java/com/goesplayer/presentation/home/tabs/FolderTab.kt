package com.goesplayer.presentation.home.tabs

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.goesplayer.OldMainActivity.todasMusicas
import com.goesplayer.R
import com.goesplayer.ResultActivity
import com.goesplayer.presentation.home.HomeList

@Composable
fun FolderTab(context: Context) {
    val foldersList = filterfolders()
    HomeList(
        title = stringResource(R.string.folder_fragment_title),
        items = foldersList,
        emptyStateMessage = stringResource(R.string.folder_tab_empty_state_message),
        onClick = { position ->
            val intent = Intent(
                context,
                ResultActivity::class.java
            )
            intent.putExtra("name", foldersList[position])
            intent.putExtra("type", ResultActivity.FOLDER)
            context.startActivity(intent)
        },
    )
}

private fun filterfolders(): List<String> =
    todasMusicas
        .mapNotNull { it.folder }
        .filter { it != "<unknown>" }
        .sortedBy { it }
        .distinct()
