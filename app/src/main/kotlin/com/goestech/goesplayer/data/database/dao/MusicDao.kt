package com.goestech.goesplayer.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.goestech.goesplayer.data.entity.Music

@Dao
interface MusicDao {
    @Query("SELECT * FROM music")
    fun getAllMusics(): List<Music>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg musics: Music)

    @Query("SELECT artist FROM music GROUP BY artist")
    fun getAllArtists(): List<String>
}
