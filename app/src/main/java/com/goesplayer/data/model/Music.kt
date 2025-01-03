package com.goesplayer.data.model

import android.net.Uri

data class Music(
    var id: Long,
    val fileName: String,
    val title: String,
    val artist: String,
    val album: String,
    val genre: String,
    val songUri: Uri,
    val albumArtUri: Uri?,
    val durationInSeconds: Long
) {
    val folder: String?
        get() {
            val path = songUri.path?.removeSuffix(fileName)
            return path?.substring(path.lastIndexOf("/") + 1)
        }
}