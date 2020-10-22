package com.goestech.goesplayer.data.repository.music

import com.goestech.goesplayer.data.Result
import com.goestech.goesplayer.data.datasource.music.MusicLocalDataSource
import com.goestech.goesplayer.data.datasource.music.DeviceStorageDataSource
import com.goestech.goesplayer.data.datasource.music.SearchMusicError
import com.goestech.goesplayer.data.datasource.playlist.PlaylistLocalDataSource
import com.goestech.goesplayer.data.entity.Music

class MusicRepositoryImpl(
    private val deviceStorageDataSource: DeviceStorageDataSource,
    private val musicLocalDataSource: MusicLocalDataSource,
    private val playlistLocalDataSource: PlaylistLocalDataSource,
) : MusicRepository {
    override suspend fun loadMusicsFromDeviceStorage(): Result<Unit, SearchMusicError> =
        deviceStorageDataSource
            .searchAllMusics()
            .mapSuccess {
                musicLocalDataSource.saveMusics(it)
            }

    override suspend fun getMusic(musicId: Long): Music = musicLocalDataSource.getMusic(musicId)

    override suspend fun getAllMusics(): List<Music> = musicLocalDataSource.getAllMusics()

    override suspend fun getAllArtists(): List<String> = musicLocalDataSource.getAllArtists()

    override suspend fun playMusic(selectedMusic: Music, playlist: List<Music>): Result<Unit, SearchMusicError> {
        playlistLocalDataSource.saveCurrentPlaylist(playlist)
        TODO("IMPLEMENT THIS")
//        playerCallback.onPlayFromMediaId(selectedMusic.musicId.toString(), null)
        return Result.Success(Unit)
    }
}
