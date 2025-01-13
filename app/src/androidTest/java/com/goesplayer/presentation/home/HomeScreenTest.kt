package com.goesplayer.presentation.home

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.Player
import com.goesplayer.AppTheme
import com.goesplayer.R
import com.goesplayer.data.model.Music
import com.goesplayer.data.model.Playlist
import com.goesplayer.presentation.MusicListRouteConfig
import com.goesplayer.presentation.PlayerViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loadHomeScreen_playerIsOffAndFloatingButtonIsHidden() {
        composeTestRule.setContent {
            AppTheme {
                initHomeScreen()
            }
        }
        val floatingButtonContentDescription =
            composeTestRule.activity.getString(R.string.home_screen_create_new_playlist_fab_content_description)

        composeTestRule
            .onNodeWithTag("mini_player")
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithContentDescription(floatingButtonContentDescription)
            .assertDoesNotExist()
    }

    @Test
    fun loadHomeScreen_playerIsOnAndFloatingButtonIsHidden() {
        composeTestRule.setContent {
            AppTheme {
                initHomeScreen(
                    playerViewState = derivedStateOf {
                        PlayerViewState.Success(
                            songName = "Test music",
                            artist = "Test singer",
                            album = null,
                            durationInMs = 40000,
                            isPlaying = false,
                            isShuffleEnabled = false,
                            repeatMode = Player.REPEAT_MODE_OFF,
                        )
                    })
            }
        }
        val floatingButtonContentDescription =
            composeTestRule.activity.getString(R.string.home_screen_create_new_playlist_fab_content_description)
        val skipToPreviousContentDescription = composeTestRule.activity.getString(R.string.skip_previous_button_content_description)
        val playButtonContentDescription =
            composeTestRule.activity.getString(R.string.play_button_content_description)
        composeTestRule.activity.getString(R.string.pause_button_content_description)
        val skipToNextContentDescription = composeTestRule.activity.getString(R.string.skip_next_button_content_description)

        composeTestRule
            .onNodeWithTag("mini_player")
            .assertExists()
        composeTestRule
            .onNodeWithContentDescription(skipToPreviousContentDescription)
            .assertExists()
        composeTestRule
            .onNodeWithContentDescription(playButtonContentDescription)
            .assertExists()
        composeTestRule
            .onNodeWithContentDescription(skipToNextContentDescription)
            .assertExists()
        composeTestRule
            .onNodeWithContentDescription(floatingButtonContentDescription)
            .assertDoesNotExist()
    }

    @Composable
    private fun initHomeScreen(
        playSong: (Music) -> Unit = {},
        createPlaylistAction: (String) -> Unit = {},
        deletePlaylistAction: (Long) -> Boolean = { false },
        loadPlaylistsRetryAction: () -> Unit = {},
        showPlaylistDetailsAction: (Playlist) -> Unit = {},
        addMusicToPlaylistAction: (Music, Playlist) -> Boolean = { _, _ -> false },
        getPlaylistsAction: () -> Flow<SearchPlaylistsState> = { flow {} },
        navigateToMusicList: (MusicListRouteConfig) -> Unit = {},
        navigateToPlayer: () -> Unit = {},
        skipToPreviousAction: () -> Unit = {},
        playOrPauseAction: () -> Unit = {},
        skipToNextAction: () -> Unit = {},
        songList: MutableLiveData<List<Music>> = MutableLiveData(listOf()),
        playerViewState: State<PlayerViewState> = derivedStateOf { PlayerViewState.None },
        playlistTabViewState: State<PlaylistTabViewState> = derivedStateOf { PlaylistTabViewState.Loading },
    ) {
        HomeScreen(
            playSong = playSong,
            createPlaylistAction = createPlaylistAction,
            deletePlaylistAction = deletePlaylistAction,
            loadPlaylistsRetryAction = loadPlaylistsRetryAction,
            showPlaylistDetailsAction = showPlaylistDetailsAction,
            addMusicToPlaylistAction = addMusicToPlaylistAction,
            getPlaylistsAction = getPlaylistsAction,
            navigateToMusicList = navigateToMusicList,
            navigateToPlayer = navigateToPlayer,
            skipToPreviousAction = skipToPreviousAction,
            playOrPauseAction = playOrPauseAction,
            skipToNextAction = skipToNextAction,
            songList = songList,
            playerViewState = playerViewState,
            playlistTabViewState = playlistTabViewState,
        )
    }
}