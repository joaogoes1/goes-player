package com.goestech.goesplayer.data.datasource.music

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.goestech.goesplayer.data.Result
import com.goestech.goesplayer.data.model.Music
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val UNKNOWN: String = "<unknown>"
private const val VOLUME_NAME: String = "external"
private const val ALBUM_ART_URI: String = "content://media/external/audio/albumart"

interface DeviceStorageDataSource {
    suspend fun searchAllMusics(): Result<List<Music>, SearchMusicError>
}

sealed class SearchMusicError {
    object UnknownError : SearchMusicError()
}
class DeviceStorageDataSourceImpl(
    private val context: Context
) : DeviceStorageDataSource {

    private val mediaMetadataRetriever = MediaMetadataRetriever()
    private val contentResolver: ContentResolver
        get() = context.contentResolver

    override suspend fun searchAllMusics(): Result<List<Music>, SearchMusicError> = withContext(Dispatchers.IO) {
        val musicList: MutableList<Music> = mutableListOf()
        val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(musicUri, null, null, null, null)
        cursor?.let { musicCursor ->
            if (musicCursor.moveToFirst()) {
                val titleColumn: Int = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val nameColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
                val artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val albumIdColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)

                do {
                    val id = musicCursor.getLong(idColumn)
                    val name = musicCursor.getString(nameColumn)
                    val title = musicCursor.getString(titleColumn)
                    val artist = musicCursor.getString(artistColumn)
                    val uri = ContentUris.withAppendedId(musicUri, id)
                    val albumId: Long = musicCursor.getLong(albumIdColumn)

                    val path = musicCursor.getString(musicCursor
                        .getColumnIndex(MediaStore.Audio.Media.DATA))

                    musicList.add(
                        Music(
                            musicId = id,
                            displayName = name,
                            title = title,
                            artist = artist,
                            albumArtUri = getAlbumArt(albumId),
                            album = getAlbumName(uri),
                            genre = getGenreName(id.toInt()),
                            duration = getDuration(uri),
                            uri = uri.toString(),
                            filePath = path ?: UNKNOWN,
                            fileName = path?.substringAfterLast("/") ?: UNKNOWN
                        )
                    )
                } while (musicCursor.moveToNext())
            }
        }
        cursor?.close()
        mediaMetadataRetriever.release()
        return@withContext Result.Success(musicList)
    }

    private fun getGenreName(id: Int): String? {
        var genre: String? = null
        val genreUri = MediaStore.Audio.Genres.getContentUriForAudioId(VOLUME_NAME, id)
        val genreCursor = contentResolver.query(genreUri, arrayOf(MediaStore.Audio.Genres.NAME), null, null, null)
        if (genreCursor?.moveToFirst() == true) {
            genre = genreCursor.getString(genreCursor.getColumnIndex(MediaStore.Audio.Genres.NAME))
        }
        genreCursor?.close()
        return genre
    }

    private fun getAlbumName(uri: Uri): String? {
        return try {
            mediaMetadataRetriever.setDataSource(context, uri)
            val album = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
            album
        } catch (e: Exception) {
            Log.e("LOAD ING MUSIC ERROR", "Failed to load album: ${uri.path}")
            null
        }
    }

    private fun getDuration(uri: Uri): Long = try {
        mediaMetadataRetriever.setDataSource(context, uri)
        val durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        durationStr.toLong()
    } catch (e: Exception) {
        Log.e("LOAD ING MUSIC ERROR", "Failed to load duration: $uri")
        0L
    }

    private fun getAlbumArt(albumId: Long): String? {
        val sArtworkUri = Uri.parse(ALBUM_ART_URI)
        return ContentUris.withAppendedId(sArtworkUri, albumId).toString()
    }
}

