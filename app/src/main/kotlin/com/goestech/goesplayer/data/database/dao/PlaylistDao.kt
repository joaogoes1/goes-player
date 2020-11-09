package com.goestech.goesplayer.data.database.dao

import androidx.room.*
import com.goestech.goesplayer.data.entity.Playlist
import com.goestech.goesplayer.data.entity.PlaylistMusicCrossRef
import com.goestech.goesplayer.data.entity.PlaylistWithMusics

const val CURRENT_PLAYLIST_NAME = "currentPlaylist"

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlist")
    fun getAllPlaylists(): List<Playlist>

    @Transaction
    @Query("SELECT * FROM playlist")
    fun getPlaylistsWithSongs(): List<PlaylistWithMusics>

    @Transaction
    @Query("SELECT * FROM playlist WHERE name = :playlistName LIMIT 1")
    fun getPlaylistWithMusics(playlistName: String): PlaylistWithMusics

    @Transaction
    @Query("SELECT * FROM playlist WHERE name = '$CURRENT_PLAYLIST_NAME' LIMIT 1")
    fun getCurrentPlaylistWithMusics(): PlaylistWithMusics

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun savePlaylist(playlist: Playlist)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCurrentPlaylist(playlistMusicCrossRefs: List<PlaylistMusicCrossRef>)

    @Query("SELECT * FROM playlistMusicCrossRef WHERE playlistId = :playlistId")
    fun getPlaylistMusicCrossRef(playlistId: Long): List<PlaylistMusicCrossRef>

    @Delete
    fun deleteCurrentPlaylistMusics(musics: List<PlaylistMusicCrossRef>)
}