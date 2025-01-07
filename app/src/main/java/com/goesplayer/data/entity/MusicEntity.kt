package com.goesplayer.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "music")
data class MusicEntity(
    @PrimaryKey val musicId: Long,
    @ColumnInfo(name = "file_name") val fileName: String,
    val title: String,
    val artist: String,
    val album: String,
    val genre: String,
    @ColumnInfo(name = "song_uri") val songUri: String,
    @ColumnInfo(name = "album_art_uri") val albumArtUri: String?,
    val durationInSeconds: Long
)
