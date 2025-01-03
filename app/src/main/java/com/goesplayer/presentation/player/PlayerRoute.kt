package com.goesplayer.presentation.player

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.goesplayer.data.model.Music
import com.goesplayer.presentation.MainActivityViewModel

@Composable
fun PlayerRoute(
    activityViewModel: MainActivityViewModel,
) {
    val context = LocalContext.current
    val music = activityViewModel.currentMusic.observeAsState()

    music.value?.let {
        PlayerScreen(
            {},
            {},
            {},
            {},
            {},
            activityViewModel.isPlaying,
            null,
            it,
            retrieveImage(it, context)
        )
    } ?: Box(modifier = Modifier.fillMaxSize()) { // TODO: Create a default loading screen
        CircularProgressIndicator(
            Modifier.align(
                Alignment.Center
            )
        )
    }
}

private fun retrieveImage(music: Music, context: Context): Bitmap? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, music.albumArtUri)
    val imgBytes = retriever.embeddedPicture
    return imgBytes?.let { BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size) }
}