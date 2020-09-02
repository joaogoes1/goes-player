package com.goestech.goesplayer.data.datasource.music

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import com.goestech.goesplayer.data.entity.MusicEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val unknown: String = "<unknown>"
private const val VOLUME_NAME: String = "external"

interface MusicStorageDataSource {
    suspend fun searchAllMusics(): List<MusicEntity>
}

class MusicStorageDataSourceImpl(
    private val context: Context
) : MusicStorageDataSource {

    private val contentResolver: ContentResolver = context.contentResolver

    override suspend fun searchAllMusics(): List<MusicEntity> = withContext(Dispatchers.IO) {
        val musicList: MutableList<MusicEntity> = mutableListOf()
        val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(musicUri, null, null, null, null)
        cursor?.let { musicCursor ->
            if (musicCursor.moveToFirst()) {
                val titleColumn: Int = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val nameColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
                val artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)

                do {
                    val id = musicCursor.getLong(idColumn)
                    val name = musicCursor.getString(nameColumn)
                    val title = musicCursor.getString(titleColumn)
                    val artist = musicCursor.getString(artistColumn)
                    val uri = ContentUris.withAppendedId(musicUri, id)

                    musicList.add(
                        MusicEntity(
                            id = id,
                            displayName = name,
                            title = title,
                            artist = artist,
                            album = getAlbumName(uri),
                            genre = getGenreName(id.toInt()),
                            filePath = uri.path ?: unknown
                        )
                    )
                } while (musicCursor.moveToNext())
            }
        }
        cursor?.close()
        return@withContext musicList
    }

    private fun getGenreName(id: Int): String {
        var genre = unknown
        val genreUri = MediaStore.Audio.Genres.getContentUriForAudioId(VOLUME_NAME, id)
        val genreCursor = contentResolver.query(genreUri, arrayOf(MediaStore.Audio.Genres.NAME), null, null, null)
        if (genreCursor?.moveToFirst() == true) {
            genre = genreCursor.getString(genreCursor.getColumnIndex(MediaStore.Audio.Genres.NAME))
                ?: unknown
        }
        genreCursor?.close()
        return genre
    }

    private fun getAlbumName(uri: Uri): String {
        return try {
            val media = MediaMetadataRetriever()
            media.setDataSource(context, uri)
            val album = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
            media.release()
            album
        } catch (e: Exception) {
            unknown
        }
    }
}