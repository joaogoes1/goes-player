package com.goesplayer.music.data.datasource.music

import com.goesplayer.commons.data.Result
import com.goesplayer.music.data.SearchMusicError
import com.goesplayer.music.data.entity.music.MusicEntity
import com.goesplayer.music.data.model.Music
import io.realm.Realm
import io.realm.RealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MusicLocalDataSourceImpl : MusicLocalDataSource {

    override suspend fun getAllMusics(): List<Music> = withContext(Dispatchers.IO) {
        val realm = Realm.getDefaultInstance()
        val result = realm
            .where(MusicEntity::class.java)
            .findAll()
            .map { it.toModel() }
        realm.close()
        result
    }

    override suspend fun getMusic(musicId: Long): Result<Music, SearchMusicError> = withContext(Dispatchers.IO) {
        val realm = Realm.getDefaultInstance()
        val result = realm
            .where(MusicEntity::class.java)
            .equalTo("musicId", musicId)
            .findFirst()
            ?.toModel()
        realm.close()
        if (result != null) {
            Result.Success(result)
        } else
            Result.Error(SearchMusicError.NotFound)
    }

    override suspend fun saveMusics(musics: List<Music>): Unit = withContext(Dispatchers.IO) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm ->
            val realmList = RealmList<MusicEntity>()
            realmList.addAll(musics.map { it.toEntity() })
            realm.insertOrUpdate(realmList)
        }
        realm.close()
    }

    override suspend fun getAllArtists(): List<String> = withContext(Dispatchers.IO) {
        val realm = Realm.getDefaultInstance()
        val result = realm
            .where(MusicEntity::class.java)
            .findAll()
            .mapNotNull { it.artist }
        realm.close()
        result
    }

    override suspend fun getAllAlbums(): List<String> = withContext(Dispatchers.IO) {
        val realm = Realm.getDefaultInstance()
        val result = realm
            .where(MusicEntity::class.java)
            .findAll()
            .mapNotNull { it.album }
        realm.close()
        result
    }

    override suspend fun getAllFolders(): List<String> = withContext(Dispatchers.IO) {
        val realm = Realm.getDefaultInstance()
        val result = realm
            .where(MusicEntity::class.java)
            .distinct("filePath")
            .findAll()
            .mapNotNull {
                it?.filePath?.replace("/${it.fileName}", "")
            }.map {
                it.substringAfterLast("/")
            }
        realm.close()
        result
    }

    override suspend fun getAllGenres(): List<String> = withContext(Dispatchers.IO) {
        val realm = Realm.getDefaultInstance()
        val result = realm
            .where(MusicEntity::class.java)
            .findAll()
            .mapNotNull { it.genre }
        realm.close()
        result
    }
}

fun MusicEntity.toModel() =
    Music(
        musicId = musicId,
        displayName = displayName,
        title = title,
        artist = artist,
        album = album,
        albumArtUri = albumArtUri,
        genre = genre,
        duration = duration,
        uri = uri,
        filePath = filePath,
        fileName = fileName,
    )

fun Music.toEntity(): MusicEntity =
    MusicEntity().also {
        it.musicId = musicId
        it.displayName = displayName
        it.title = title
        it.artist = artist
        it.album = album
        it.albumArtUri = albumArtUri
        it.genre = genre
        it.duration = duration
        it.uri = uri
        it.filePath = filePath
        it.fileName = fileName
    }