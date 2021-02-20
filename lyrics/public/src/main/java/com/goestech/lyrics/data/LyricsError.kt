package com.goestech.lyrics.data

sealed class LyricsError {
    object NetworkError : LyricsError()
    object UnknownError : LyricsError()
}