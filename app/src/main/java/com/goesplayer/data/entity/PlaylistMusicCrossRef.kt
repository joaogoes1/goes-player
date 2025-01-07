package com.goesplayer.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation


@Entity(primaryKeys = ["playlistId", "musicId"])
data class PlaylistSongCrossRef(
    val playlistId: Long,
    val musicId: Long
)

data class PlaylistWithSongs(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "musicId",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val musics: List<MusicEntity>
)
