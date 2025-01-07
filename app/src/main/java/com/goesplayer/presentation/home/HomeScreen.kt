package com.goesplayer.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.MutableLiveData
import com.goesplayer.R
import com.goesplayer.data.model.Music
import com.goesplayer.data.model.Playlist
import com.goesplayer.presentation.MusicListRouteConfig
import com.goesplayer.presentation.PlayerViewState
import com.goesplayer.presentation.components.MiniPlayer
import com.goesplayer.presentation.home.tabs.AlbumTab
import com.goesplayer.presentation.home.tabs.ArtistTab
import com.goesplayer.presentation.home.tabs.FolderTab
import com.goesplayer.presentation.home.tabs.GenreTab
import com.goesplayer.presentation.home.tabs.HomeTab
import com.goesplayer.presentation.home.tabs.MusicTab
import com.goesplayer.presentation.home.tabs.PlaylistTab
import com.goesplayer.presentation.home.tabs.PlaylistTabDialogState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    playSong: (Music) -> Unit,
    createPlaylistAction: (String) -> Unit,
    deletePlaylistAction: (Long) -> Boolean,
    loadPlaylistsRetryAction: () -> Unit,
    showPlaylistDetailsAction: (Playlist) -> Unit,
    addMusicToPlaylistAction: (Music, Playlist) -> Boolean,
    getPlaylistsAction: () -> Flow<SearchPlaylistsState>,
    navigateToMusicList: (MusicListRouteConfig) -> Unit,
    navigateToPlayer: () -> Unit,
    skipToPreviousAction: () -> Unit,
    playOrPauseAction: () -> Unit,
    skipToNextAction: () -> Unit,
    songList: MutableLiveData<List<Music>>,
    playerViewState: State<PlayerViewState>,
    playlistTabViewState: State<PlaylistTabViewState>
) {
    val scope = rememberCoroutineScope()
    val musicsState = songList.observeAsState()
    val pagerState = rememberPagerState(pageCount = { 7 })
    val playlistTabDialogState =
        remember { mutableStateOf<PlaylistTabDialogState>(PlaylistTabDialogState.None) }

    Scaffold(
        topBar = {
            // TODO: Add search button
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                title = {
                    Text(
                        stringResource(R.string.app_name),
                    )
                },
            )
        },
        floatingActionButton = {
            HomeFloatingButton(
                { playlistTabDialogState.value = PlaylistTabDialogState.CreatePlaylist },
                pagerState,
            )
        },
        bottomBar = {
            MiniPlayer(
                skipToPreviousAction,
                playOrPauseAction,
                skipToNextAction,
                navigateToPlayer,
                playerViewState,
            )
        }
    ) { innerPadding ->
        val tabs = listOf(
            Icons.Filled.Home,
            Icons.AutoMirrored.Filled.PlaylistPlay,
            Icons.Filled.MusicNote,
            Icons.Filled.Person,
            Icons.Filled.Album,
            Icons.Filled.LibraryMusic,
            Icons.Filled.Folder,
        )
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            TabRow(
                containerColor = MaterialTheme.colorScheme.background,
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                tabs.forEachIndexed { index, icon ->
                    Tab(
                        icon = { Icon(icon, contentDescription = null) },
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.scrollToPage(index) } },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.LightGray,
                    )
                }
            }
            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> HomeTab()
                    1 -> PlaylistTab(
                        deletePlaylistAction = deletePlaylistAction,
                        createPlaylistAction = createPlaylistAction,
                        showDeletePlaylistDialog = { id, name ->
                            playlistTabDialogState.value =
                                PlaylistTabDialogState.DeletePlaylist(id, name)
                        },
                        showPlaylistDetailsAction = showPlaylistDetailsAction,
                        onDismissRequest = {
                            playlistTabDialogState.value = PlaylistTabDialogState.None
                        },
                        retryAction = loadPlaylistsRetryAction,
                        playlistTabViewState = playlistTabViewState,
                        playlistTabDialogState = playlistTabDialogState.value,
                    )

                    2 -> MusicTab(
                        playSong = playSong,
                        addMusicToPlaylistAction = addMusicToPlaylistAction,
                        getPlaylistsAction = getPlaylistsAction,
                        songList = musicsState.value ?: emptyList(),
                    )

                    3 -> ArtistTab(
                        openArtistMusics = navigateToMusicList,
                        songList = musicsState.value ?: emptyList(),
                    )

                    4 -> AlbumTab(
                        openAlbumMusics = navigateToMusicList,
                        songList = musicsState.value ?: emptyList(),
                    )

                    5 -> GenreTab(
                        openGenreMusics = navigateToMusicList,
                        songList = musicsState.value ?: emptyList(),
                    )

                    6 -> FolderTab(
                        openFolderMusics = navigateToMusicList,
                        songList = musicsState.value ?: emptyList(),
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeFloatingButton(
    showCreatePlaylistAction: () -> Unit,
    viewPagerState: PagerState,
) {
    if (viewPagerState.currentPage != 1) return

    FloatingActionButton(
        onClick = showCreatePlaylistAction,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = CircleShape,
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = stringResource(R.string.home_screen_create_new_playlist_fab_content_description),
        )
    }
}
