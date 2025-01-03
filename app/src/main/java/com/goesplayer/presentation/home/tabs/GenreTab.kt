package com.goesplayer.presentation.home.tabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.goesplayer.R
import com.goesplayer.data.model.Music
import com.goesplayer.presentation.components.SingleTextList

@Composable
fun GenreTab(
    openGenreMusics: (String) -> Unit,
    songList: List<Music>,
) {
    val genresList = songList.filterGenres()
    SingleTextList(
        title = stringResource(R.string.genre_fragment_title),
        items = genresList,
        emptyStateMessage = stringResource(R.string.genre_tab_empty_state_message),
        onClick = { position ->
            openGenreMusics(genresList[position])
        },
    )
}

private fun List<Music>.filterGenres() =
    this
        .map { it.genre }
        .filter { it != "<unknown>" }
        .sortedBy { it }
        .distinct()
