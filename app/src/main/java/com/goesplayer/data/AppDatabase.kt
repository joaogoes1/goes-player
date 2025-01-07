package com.goesplayer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.goesplayer.data.dao.MusicDao
import com.goesplayer.data.dao.PlaylistDao
import com.goesplayer.data.entity.MusicEntity
import com.goesplayer.data.entity.PlaylistEntity
import com.goesplayer.data.entity.PlaylistSongCrossRef

@Database(
    entities = [
        MusicEntity::class,
        PlaylistEntity::class,
        PlaylistSongCrossRef::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
    abstract fun playlistDao(): PlaylistDao
}