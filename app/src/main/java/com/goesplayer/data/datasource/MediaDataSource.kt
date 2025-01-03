package com.goesplayer.data.datasource

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import com.goesplayer.data.model.Music
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MediaDataSource @Inject constructor(
    @ApplicationContext private val context: Context,

) {
    // TODO: Review this
    fun loadSongs(): List<Music> {
        val contentResolver = context.contentResolver
        val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val musicCursor = contentResolver.query(musicUri, null, null, null, null)
        val musics = mutableListOf<Music>()

        if (musicCursor != null && musicCursor.moveToFirst()) {
            val titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val nameColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
            val artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)


            do {
                val id = musicCursor.getLong(idColumn)
                val thisTitle = musicCursor.getString(titleColumn)
                val thisArtist = musicCursor.getString(artistColumn)
                var thisAlbum = "<unknown>"
                val thisName = musicCursor.getString(nameColumn)
                val thisUri = ContentUris.withAppendedId(musicUri, id)
                var thisgenre = "<unknown>"

                val genreUri =
                    MediaStore.Audio.Genres.getContentUriForAudioId("external", id.toInt())
                val genreCursor = contentResolver.query(
                    genreUri,
                    arrayOf(MediaStore.Audio.Genres.NAME),
                    null,
                    null,
                    null
                )
                if (genreCursor!!.moveToFirst()) {
                    thisgenre =
                        genreCursor!!.getString(genreCursor!!.getColumnIndex(MediaStore.Audio.Genres.NAME))
                }
                genreCursor!!.close()

                val media = MediaMetadataRetriever()
                try {
                    media.setDataSource(context, thisUri)
                    thisAlbum =
                        media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: ""
                    media.release()
                } catch (e: Exception) {
                    //Ignore it
                }

                musics.add(
                    Music(
                        id,
                        thisName,
                        thisTitle,
                        thisArtist,
                        thisAlbum,
                        thisgenre,
                        musicUri,
                        thisUri,
                        musicCursor.getLong(musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) * 1000,
                    )
                )
            } while (musicCursor.moveToNext())
        }
        musicCursor?.close()
        return musics
    }
}