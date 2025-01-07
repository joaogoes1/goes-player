package com.goesplayer.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.goesplayer.data.entity.MusicEntity

@Dao
interface MusicDao {
    @Query("SELECT * FROM music")
    suspend fun getAll(): List<MusicEntity>

    @Query("SELECT * FROM music WHERE musicId = :musicId")
    suspend fun findById(musicId: Long): MusicEntity
}
