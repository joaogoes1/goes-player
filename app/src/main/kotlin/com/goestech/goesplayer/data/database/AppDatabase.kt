package com.goestech.goesplayer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.goestech.goesplayer.data.database.dao.MusicDao
import com.goestech.goesplayer.data.database.dao.PlaylistDao
import com.goestech.goesplayer.data.entity.Music
import com.goestech.goesplayer.data.entity.Playlist
import com.goestech.goesplayer.data.entity.PlaylistMusicCrossRef
import com.goestech.goesplayer.data.entity.PlaylistWithMusics

@Database(entities = [Music::class, Playlist::class, PlaylistMusicCrossRef::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
    abstract fun playlistDao(): PlaylistDao
}
