package com.goesplayer.music.data.entity.music

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MusicEntity : RealmObject() {
    @PrimaryKey var musicId: Long = 1
    var displayName: String? = null
    var title: String? = null
    var artist: String? = null
    var album: String? = null
    var albumArtUri: String? = null
    var genre: String? = null
    var duration: Long = 0
    var uri: String = ""
    var filePath: String = ""
    var fileName: String = ""
}