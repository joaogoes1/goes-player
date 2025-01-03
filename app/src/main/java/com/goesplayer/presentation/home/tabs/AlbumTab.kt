package com.goesplayer.presentation.home.tabs

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.goesplayer.R
import com.goesplayer.data.model.Music
import com.goesplayer.presentation.components.DoubleTextWithAlbumArtList
import com.goesplayer.presentation.components.DoubleTextWithAlbumItemView

@Composable
fun AlbumTab(
    openAlbumMusics: (String, String) -> Unit,
    songList: List<Music>,
) {
    val albumsList = songList.filterAlbums()
    DoubleTextWithAlbumArtList(
        title = stringResource(R.string.album_fragment_title),
        items = albumsList,
        emptyStateMessage = stringResource(R.string.album_tab_empty_state_message),
        onClick = { position ->
            val album = albumsList[position]
            openAlbumMusics(album.artist, album.name)
        },
    )
}

private fun List<Music>.filterAlbums(): List<DoubleTextWithAlbumItemView> =
    this
        .filter { it.artist != "<unknown>" || it.album == "<unknown>" }
        .groupBy { Triple(it.artist, it.album, it.albumArtUri) }
        .keys
        .map {
            DoubleTextWithAlbumItemView(
                it.hashCode().toLong(),
                it.second,
                it.first,
                it.third ?: Uri.EMPTY,
                null,
            )
        }
