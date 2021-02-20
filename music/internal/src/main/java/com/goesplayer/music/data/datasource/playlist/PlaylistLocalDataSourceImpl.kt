package com.goesplayer.music.data.datasource.playlist

import com.goesplayer.music.data.datasource.music.toEntity
import com.goesplayer.music.data.datasource.music.toModel
import com.goesplayer.music.data.entity.music.MusicEntity
import com.goesplayer.music.data.entity.playlist.PlaylistEntity
import com.goesplayer.music.data.model.Music
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val CURRENT_PLAYLIST_NAME = "current"

class PlaylistLocalDataSourceImpl(
//    private val realm: Realm
) : PlaylistLocalDataSource {
    override suspend fun saveCurrentPlaylist(musicList: List<Music>) = withContext(Dispatchers.IO) {
        val config = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(config)
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm ->
            val currentPlaylist = realm.where(PlaylistEntity::class.java).findFirst()!!
            val realmList = RealmList<MusicEntity>()
            realmList.addAll(musicList.map { it.toEntity() })
            currentPlaylist.musics = realmList
        }
    }

    override suspend fun getCurrentPlaylist(): List<Music> = withContext(Dispatchers.IO) {
        val config = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(config)
        val realm = Realm.getDefaultInstance()
        val result = realm
            .where(PlaylistEntity::class.java)
            .equalTo(CURRENT_PLAYLIST_NAME, "current")
            .findFirst()!!
            .musics.map { it.toModel() }
        realm.close()
        result
    }

    override suspend fun getAllPlaylists(): List<String> = withContext(Dispatchers.IO) {
        val config = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(config)
        val realm = Realm.getDefaultInstance()
        val result = realm
            .where(PlaylistEntity::class.java)
            .findAll()
            .map { it.name }
        realm.close()
        result
    }
}
