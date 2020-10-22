package com.goestech.goesplayer.data.datasource.playlist

import com.goestech.goesplayer.data.database.dao.PlaylistDao
import com.goestech.goesplayer.data.entity.Music
import com.goestech.goesplayer.data.entity.PlaylistMusicCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaylistLocalDataSourceImpl(
    private val playlistDao: PlaylistDao
) : PlaylistLocalDataSource {
    override suspend fun saveCurrentPlaylist(musicList: List<Music>) = withContext(Dispatchers.IO) {
        playlistDao.saveCurrentPlaylist(emptyList())
        val currentPlaylist = playlistDao.getPlaylistWithMusics("currentPlaylist")
        val currentPlaylistMusicCrossRef = playlistDao.getPlaylistMusicCrossRef(currentPlaylist.playlist.playlistId)
        playlistDao.deleteCurrentPlaylistMusics(currentPlaylistMusicCrossRef)
        playlistDao.saveCurrentPlaylist(
            musicList.map {
                PlaylistMusicCrossRef(
                    playlistId = currentPlaylist.playlist.playlistId,
                    musicId = it.musicId
                )
            }
        )
    }

    override suspend fun getCurrentPlaylist(): List<Music> = withContext(Dispatchers.IO) {
        playlistDao.getCurrentPlaylistWithMusics().musics
    }
}