package com.goesplayer.presentation.musiclist

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.goesplayer.R
import com.goesplayer.data.model.Music
import com.goesplayer.presentation.components.DoubleTextWithAlbumArtList
import com.goesplayer.presentation.components.DoubleTextWithAlbumItemView

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicListScreen(
    title: String,
    songList: List<Music>,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(
                            R.string.back_button_content_description
                        )
                    )
                }
            )
        }
    ) {
        DoubleTextWithAlbumArtList(
            onClick = {},
            onLongClick = {},
            title = "Músicas",
            emptyStateMessage = "",
            items = songList.map {
                DoubleTextWithAlbumItemView(
                    it.id,
                    it.title,
                    it.artist,
                    it.albumArtUri ?: Uri.EMPTY,
                    thumb = null,
                )
            },
        )
    }
}