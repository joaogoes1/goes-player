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
fun GenreTab(context: Context) {
    val genresList = filterGenres()
    Box(modifier = Modifier.fillMaxSize()) {
        HomeList(
            title = stringResource(R.string.genre_fragment_title),
            items = genresList,
            onClick = { position ->
                val intent = Intent(
                    context,
                    ResultActivity::class.java
                )
                intent.putExtra("name", genresList[position])
                intent.putExtra("type", ResultActivity.GENDER)
                context.startActivity(intent)
            },
        )
    }
}

private fun filterGenres() =
    todasMusicas
        .map { it.genre }
        .filter { it != "<unknown>" }
        .sortedBy { it }
        .distinct()
