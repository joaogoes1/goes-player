package com.goestech.goesplayer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.goestech.goesplayer.data.database.dao.MusicDao
import com.goestech.goesplayer.data.entity.Music

@Database(entities = [Music::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
}
