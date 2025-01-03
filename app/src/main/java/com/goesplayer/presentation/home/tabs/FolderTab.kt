package com.goesplayer.presentation.home.tabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.goesplayer.R
import com.goesplayer.data.model.Music
import com.goesplayer.presentation.components.SingleTextList

@Composable
fun FolderTab(
    openFolderMusics: (String) -> Unit,
    songList: List<Music>,
) {
    val foldersList = songList.filterFolders()
    SingleTextList(
        title = stringResource(R.string.folder_fragment_title),
        items = foldersList,
        emptyStateMessage = stringResource(R.string.folder_tab_empty_state_message),
        onClick = { position ->
            openFolderMusics(foldersList[position])
        },
    )
}

private fun List<Music>.filterFolders(): List<String> =
    this
        .mapNotNull { it.folder }
        .filter { it != "<unknown>" }
        .sortedBy { it }
        .distinct()
