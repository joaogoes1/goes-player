package com.goesplayer.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.goesplayer.data.entity.MusicEntity
import com.goesplayer.data.entity.PlaylistEntity
import com.goesplayer.data.entity.PlaylistSongCrossRef
import com.goesplayer.data.entity.PlaylistWithSongs

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlist")
    suspend fun getAll(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist WHERE playlistId = :playlistId")
    suspend fun findById(playlistId: Int): PlaylistEntity

    @Transaction
    @Query("SELECT * FROM playlist WHERE playlistId = :playlistId")
    suspend fun getPlaylistWithSongs(playlistId: Long): PlaylistWithSongs

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(playlist: PlaylistEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE) // TODO: Ask for the user before add repeated musics in a Playlist
    suspend fun insertMusic(ref: PlaylistSongCrossRef)

    @Query("DELETE FROM playlist WHERE playlistId = :id")
    suspend fun delete(id: Long)
}