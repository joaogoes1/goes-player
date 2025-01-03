package com.goesplayer.presentation.home.tabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.goesplayer.R
import com.goesplayer.data.model.Music
import com.goesplayer.presentation.components.SingleTextList
import com.goesplayer.presentation.musiclist.Artist
import com.goesplayer.presentation.musiclist.SearchProperties

@Composable
fun ArtistTab(
    openArtistMusics: (String, SearchProperties) -> Unit,
    songList: List<Music>
) {
    val artistsList = songList.filterAlbums()
    SingleTextList(
        title = stringResource(R.string.artist_fragment_title),
        items = artistsList,
        emptyStateMessage = stringResource(R.string.artist_tab_empty_state_message),
        onClick = { position ->
            openArtistMusics(artistsList[position], Artist(artistsList[position]))
        },
    )
}

private fun List<Music>.filterAlbums() =
    this
        .map { it.artist }
        .filter { it != "<unknown>" }
        .sortedBy { it }
        .distinct()
