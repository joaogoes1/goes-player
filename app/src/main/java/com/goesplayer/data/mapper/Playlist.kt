package com.goesplayer.data.mapper

import com.goesplayer.data.entity.PlaylistEntity
import com.goesplayer.data.model.Playlist


fun PlaylistEntity.toModel() =
    Playlist(
        id = playlistId,
        name = name,
    )

fun List<PlaylistEntity>.toModel() =
    map { it.toModel() }