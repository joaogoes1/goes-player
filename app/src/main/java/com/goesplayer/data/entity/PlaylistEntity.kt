package com.goesplayer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "playlist")
@Serializable
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val playlistId: Long = 0L,
    val name: String,
)
