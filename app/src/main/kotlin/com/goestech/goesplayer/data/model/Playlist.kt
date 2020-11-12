package com.goestech.goesplayer.data.entity

import androidx.room.*

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Playlist(
    @PrimaryKey val playlistId: Long,
    val name: String
)

@Entity(primaryKeys = ["playlistId", "musicId"])
data class PlaylistMusicCrossRef(
    val playlistId: Long,
    val musicId: Long
)

data class PlaylistWithMusics(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "musicId",
        associateBy = Junction(PlaylistMusicCrossRef::class)
    )
    val musics: List<Music>
)