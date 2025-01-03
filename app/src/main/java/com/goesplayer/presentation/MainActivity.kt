package com.goesplayer.presentation

import android.content.ComponentName
import android.content.ContentUris
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.OptIn
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.goesplayer.BancoController
import com.goesplayer.R
import com.goesplayer.data.model.Music
import com.goesplayer.data.model.Playlist
import com.goesplayer.presentation.home.HomeFragment
import com.goesplayer.presentation.player.PlayerFragment
import com.goesplayer.presentation.player.PlayerService
import com.google.common.util.concurrent.MoreExecutors


class MainActivity : FragmentActivity() {
    private var crud: BancoController? = null
    var controller: MediaController? = null

    // TODO: Review this after the arch migration
    var playlists: MutableLiveData<List<Playlist>> = MutableLiveData(emptyList())
    var isLoadingPlaylists: MutableLiveData<Boolean> = MutableLiveData(true)
    val todasMusicas: MutableLiveData<List<Music>> = MutableLiveData()
    val currentMusic: MutableLiveData<Music?> = MutableLiveData()
    val isPlaying: MutableState<Boolean> = mutableStateOf(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view, HomeFragment::class.java, null)
            .setReorderingAllowed(true)
            .commit()
        crud = BancoController(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, PlayerService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                controller = controllerFuture.get()
                controller?.addListener(object : Player.Listener {
                    override fun onEvents(player: Player, events: Player.Events) {
                        if (isPlaying.value != player.isPlaying)
                            isPlaying.value = player.isPlaying
                    }

                    @OptIn(UnstableApi::class)
                    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                        val music = Music(
                            id = 1,
                            fileName = mediaMetadata.displayTitle?.toString() ?: "",
                            title = mediaMetadata.title?.toString() ?: "",
                            artist = mediaMetadata.artist?.toString() ?: "",
                            album = mediaMetadata.albumTitle?.toString() ?: "",
                            genre = mediaMetadata.genre?.toString() ?: "",
                            songUri = mediaMetadata.artworkUri ?: Uri.EMPTY,
                            durationInSeconds = mediaMetadata.durationMs?.times(1000) ?: 0,
                            albumArtUri = mediaMetadata.artworkUri
                        )
                        currentMusic.value = music
                    }
                })
            },
            MoreExecutors.directExecutor()
        )
        playlists.postValue(crud?.loadPlaylists())
        isLoadingPlaylists.postValue(false)
        loadSongs()
    }

    fun playSong(music: Music) {
        controller?.setMediaItem(MediaItem.Builder().setUri(music.songUri).setMediaMetadata(
            MediaMetadata.Builder().setArtworkUri(music.albumArtUri).build()
        ).build())
        controller?.prepare()
        controller?.play()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container_view, PlayerFragment::class.java, null)
            .setReorderingAllowed(true)
            .commit()
    }

    // TODO: Review this
    private fun loadSongs() {
       val musicResolver = contentResolver
        val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val musicCursor = musicResolver.query(musicUri, null, null, null, null)
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
                    media.setDataSource(this, thisUri)
                    thisAlbum = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: ""
                    media.release()
                } catch (e: Exception) {
                    //Ignore it
                }

                if (thisAlbum == null) thisAlbum = "<unknown>"

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
        todasMusicas.postValue(musics)
    }
}