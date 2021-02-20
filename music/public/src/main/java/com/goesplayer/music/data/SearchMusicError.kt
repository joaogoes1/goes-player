package com.goesplayer.music.data


sealed class SearchMusicError {
    object UnknownError : SearchMusicError()
    object NotFound : SearchMusicError()
}