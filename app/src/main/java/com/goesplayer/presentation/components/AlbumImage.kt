package com.goesplayer.presentation.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

@Composable
fun AlbumImage(
    modifier: Modifier = Modifier,
    albumUri: Uri?,
) {
    val albumArt = albumUri?.retrieveImage(LocalContext.current)
    if (albumArt != null) {
        Image(
            bitmap = albumArt.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .aspectRatio(1f)
        )
    } else {
        Icon(
            Icons.Filled.Album,
            contentDescription = null,
            tint = Color.Gray,
            modifier = modifier
                .aspectRatio(1f)
                .background(Color(0xFF151515))
        )
    }
}

fun Uri.retrieveImage(context: Context): Bitmap? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, this)
    val imgBytes = retriever.embeddedPicture
    return imgBytes?.let { BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size) }
}
