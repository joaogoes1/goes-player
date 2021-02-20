package com.goesplayer.music.data.entity.playlist

import com.goesplayer.music.data.entity.music.MusicEntity
import com.goesplayer.music.data.model.Music
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PlaylistEntity : RealmObject() {
    @PrimaryKey var playlistId: Long = 1
    var name: String = ""
    var musics: RealmList<MusicEntity> = RealmList()
}