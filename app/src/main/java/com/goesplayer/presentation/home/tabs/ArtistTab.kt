package com.goesplayer.presentation.home.tabs

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.goesplayer.MainActivity
import com.goesplayer.MainActivity.todasMusicas
import com.goesplayer.R
import com.goesplayer.ResultActivity
import com.goesplayer.presentation.home.HomeList

@Composable
fun ArtistTab(
    context: Context
) {
    val artistsList = filterAlbums()
    Box(modifier = Modifier.fillMaxSize()) {
        HomeList(
            title = stringResource(R.string.artist_fragment_title),
            items = artistsList,
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
}

private fun filterAlbums() =
    todasMusicas
        .map { it.artist }
        .filter { it != "<unknown>" }
        .sortedBy { it }
        .distinct()