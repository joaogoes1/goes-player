package com.goesplayer.presentation

import android.content.ComponentName
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.goesplayer.presentation.player.PlayerService
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoesPlayerApp(viewModel)
        }
    }

    @OptIn(UnstableApi::class)
    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, PlayerService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                viewModel.controller = controllerFuture.get()
                viewModel.controller?.addListener(object : Player.Listener {
                    override fun onEvents(player: Player, events: Player.Events) {
                        viewModel.updatePlayerStatus(
                            PlayerViewState.Success(
                                songName = player.mediaMetadata.title.toString(),
                                artist = player.mediaMetadata.artist.toString(),
                                album = player.mediaMetadata.artworkUri,
                                durationInMs = max(0L, player.duration),
                                isPlaying = player.isPlaying,
                                isShuffleEnabled = player.shuffleModeEnabled,
                                repeatMode = player.repeatMode,
                            )
                        )
                    }
                })
            },
            MoreExecutors.directExecutor()
        )
        viewModel.loadSongs()
    }
}