package com.goesplayer.presentation.player

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.goesplayer.data.model.Music
import com.goesplayer.presentation.MainActivityViewModel
import com.goesplayer.presentation.components.LoadingScreen

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
            { activityViewModel.playOrPause() },
            {},
            {},
            activityViewModel.isPlaying,
            null,
            it,
            retrieveImage(it, context)
        )
    } ?: LoadingScreen()
}

private fun retrieveImage(music: Music, context: Context): Bitmap? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, music.albumArtUri)
    val imgBytes = retriever.embeddedPicture
    return imgBytes?.let { BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size) }
}