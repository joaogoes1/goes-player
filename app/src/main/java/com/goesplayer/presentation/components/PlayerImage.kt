package com.goesplayer.presentation.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.goesplayer.R

@Composable
fun PlayerImage(
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
        Image(
            painter = painterResource(R.mipmap.teste_album),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .aspectRatio(1f)
        )
    }
}

fun Uri.retrieveImage(context: Context): Bitmap? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, this)
    val imgBytes = retriever.embeddedPicture
    return imgBytes?.let { BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size) }
}
