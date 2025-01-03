package com.goesplayer.presentation

import android.content.ComponentName
import android.content.ContentUris
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.crud = BancoController(applicationContext)
        setContent {
            GoesPlayerApp(viewModel)
        }
    }

    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, PlayerService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                viewModel.controller = controllerFuture.get()
                viewModel.controller?.addListener(object : Player.Listener {
                    override fun onEvents(player: Player, events: Player.Events) {
                        if (viewModel.isPlaying.value != player.isPlaying)
                            viewModel.isPlaying.value = player.isPlaying
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
                        viewModel.currentMusic.value = music
                    }
                })
            },
            MoreExecutors.directExecutor()
        )
        viewModel.playlists.postValue(viewModel.crud?.loadPlaylists())
        viewModel.isLoadingPlaylists.postValue(false)
        viewModel.loadSongs()
    }

    fun playSong(music: Music) {
        viewModel.controller?.setMediaItem(MediaItem
            .Builder()
            .setUri(music.songUri)
            .setMediaMetadata(MediaMetadata
                .Builder()
                .setArtworkUri(music.albumArtUri)
                .build()
            ).build())
        viewModel.controller?.prepare()
        viewModel.controller?.play()
        // TODO: Navigate to PlayerScreen
    }
}