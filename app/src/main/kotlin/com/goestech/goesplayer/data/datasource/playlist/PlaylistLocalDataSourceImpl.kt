package com.goestech.goesplayer.data.datasource.playlist

import com.goestech.goesplayer.data.database.dao.CURRENT_PLAYLIST_NAME
import com.goestech.goesplayer.data.database.dao.PlaylistDao
import com.goestech.goesplayer.data.model.Music
import com.goestech.goesplayer.data.model.PlaylistMusicCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaylistLocalDataSourceImpl(
    private val playlistDao: PlaylistDao
) : PlaylistLocalDataSource {
    override suspend fun saveCurrentPlaylist(musicList: List<Music>) = withContext(Dispatchers.IO) {
        playlistDao.saveCurrentPlaylist(emptyList())
        val currentPlaylist = playlistDao.getPlaylistWithMusics(CURRENT_PLAYLIST_NAME)
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

    override suspend fun getAllPlaylists(): List<String> = withContext(Dispatchers.IO) {
        playlistDao.getAllPlaylists().map {
            it.name
        }
    }
}
