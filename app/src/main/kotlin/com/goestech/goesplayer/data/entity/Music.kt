package com.goestech.goesplayer.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Music(
    @PrimaryKey val musicId: Long,
    val displayName: String?,
    val title: String?,
    val artist: String?,
    val album: String?,
    val albumArtUri: String?,
    val genre: String?,
    val uri: String,
    val filePath: String,
    val fileName: String
) : Parcelable
