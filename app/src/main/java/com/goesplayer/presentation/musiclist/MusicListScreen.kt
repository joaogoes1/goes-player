package com.goesplayer.presentation.musiclist

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.goesplayer.R
import com.goesplayer.data.model.Music
import com.goesplayer.presentation.components.DoubleTextWithAlbumArtList
import com.goesplayer.presentation.components.DoubleTextWithAlbumItemView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicListScreen(
    title: String,
    songList: List<Music>,
) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                navigationIcon = if (navController.previousBackStackEntry != null) {
                    {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_button_content_description),
                                tint = MaterialTheme.typography.bodyLarge.color
                            )
                        }
                    }
                } else {
                    {}
                }
            )
        }
    ) { innerPadding ->
        DoubleTextWithAlbumArtList(
            modifier = Modifier.padding(innerPadding),
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