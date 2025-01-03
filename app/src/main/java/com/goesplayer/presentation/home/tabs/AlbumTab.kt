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
fun AlbumTab(
    context: Context
) {
    val albumsList = filterAlbums()
    HomeList(
        title = stringResource(R.string.album_fragment_title),
        items = albumsList,
        emptyStateMessage = stringResource(R.string.album_tab_empty_state_message),
        onClick = { position ->
            val intent = Intent(
                context,
                ResultActivity::class.java
            )
            intent.putExtra("name", albumsList[position])
            intent.putExtra("type", ResultActivity.ALBUM)
            context.startActivity(intent)
        },
    )
}

private fun filterAlbums() =
    todasMusicas
        .map { it.album }
        .filter { it != "<unknown>" }
        .sortedBy { it }
        .distinct()