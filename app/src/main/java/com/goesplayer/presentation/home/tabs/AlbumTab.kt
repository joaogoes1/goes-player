package com.goesplayer.presentation.home.tabs

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Box(modifier = Modifier.fillMaxSize()) {
        HomeList(
            title = stringResource(R.string.album_fragment_title),
            items = albumsList,
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
}

private fun filterAlbums() =
    todasMusicas
        .map { it.album }
        .filter { it != "<unknown>" }
        .sortedBy { it }
        .distinct()