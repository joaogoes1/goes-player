package com.goesplayer.data.mapper

import android.net.Uri
import com.goesplayer.data.entity.MusicEntity
import com.goesplayer.data.model.Music


fun Music.toEntity(): MusicEntity =
    MusicEntity(
        musicId = id,
        fileName = fileName,
        title = title,
        artist = artist,
        album = album,
        genre = genre,
        songUri = songUri.toString(),
        albumArtUri = albumArtUri?.toString(),
        durationInSeconds = durationInSeconds,
    )

fun List<Music>.toEntity(): List<MusicEntity> =
    map { it.toEntity() }

fun MusicEntity.toModel(): Music =
    Music(
        id = musicId,
        fileName = fileName,
        title = title,
        artist = artist,
        album = album,
        genre = genre,
        songUri = Uri.parse(songUri),
        albumArtUri = albumArtUri?.let { Uri.parse(it) },
        durationInSeconds = durationInSeconds,
    )

fun List<MusicEntity>.toModel(): List<Music> =
    map { it.toModel() }
