package com.goestech.goesplayer.data.mapper

import com.goestech.goesplayer.bussiness.model.MusicModel
import com.goestech.goesplayer.data.entity.MusicEntity

fun MusicEntity.toModel() = MusicModel(
    id = id,
    displayName = displayName,
    title = title,
    artist = artist,
    album = album,
    genre = genre,
    folder = getFolder(filePath),
    fileName = getFileName(filePath),
    completePath = filePath
)

private fun getFileName(filePath: String): String = filePath.substringAfterLast("/")

private fun getFolder(path: String): String {
    val lastSlash = path.lastIndexOf("/")
    val penultimateSlash = path.substringBeforeLast("/").lastIndexOf("/")
    return path.substring(penultimateSlash, lastSlash)
}