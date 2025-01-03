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
fun ArtistTab(
    context: Context
) {
    val artistsList = filterAlbums()
    HomeList(
        title = stringResource(R.string.artist_fragment_title),
        items = artistsList,
        emptyStateMessage = stringResource(R.string.artist_tab_empty_state_message),
        onClick = { position ->
            val intent = Intent(
                context,
                ResultActivity::class.java
            )
            intent.putExtra("name", artistsList[position])
            intent.putExtra("type", ResultActivity.ARTISTA)
            context.startActivity(intent)
        },
    )
}

private fun filterAlbums() =
    todasMusicas
        .map { it.artist }
        .filter { it != "<unknown>" }
        .sortedBy { it }
        .distinct()