package com.goesplayer.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.goesplayer.data.entity.MusicEntity
import com.goesplayer.data.model.Music

@Dao
interface MusicDao {
    @Query("SELECT * FROM music")
    suspend fun getAll(): List<MusicEntity>

    @Query("SELECT * FROM music WHERE musicId = :musicId")
    suspend fun findById(musicId: Long): MusicEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(musics: List<MusicEntity>)
}
