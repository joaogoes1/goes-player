package com.goestech.goesplayer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Music(
    @PrimaryKey val id: Long,
    val displayName: String?,
    val title: String?,
    val artist: String?,
    val album: String?,
    val albumArtUri: String?,
    val genre: String?,
    val filePath: String,
    val fileName: String
)
