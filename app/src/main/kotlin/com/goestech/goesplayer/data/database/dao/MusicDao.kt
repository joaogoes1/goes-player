package com.goestech.goesplayer.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.goestech.goesplayer.data.model.Music

@Dao
interface MusicDao {
    @Query("SELECT * FROM music")
    fun getAllMusics(): List<Music>

    @Query("SELECT * FROM music WHERE musicId = :musicId")
    fun getMusic(musicId: Long): Music

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg musics: Music)

    @Query("SELECT artist FROM music GROUP BY artist")
    fun getAllArtists(): List<String>

    @Query("SELECT album FROM music GROUP BY album")
    fun getAllAlbums(): List<String?>

    @Query("SELECT filePath FROM music GROUP BY filePath")
    fun getAllFolders(): List<String?>

    @Query("SELECT genre FROM music GROUP BY genre")
    fun getAllGenres(): List<String?>
}
