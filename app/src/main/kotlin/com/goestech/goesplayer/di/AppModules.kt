package com.goestech.goesplayer.di

import com.goesplayer.lyrics.di.lyricsModule
import com.goesplayer.lyrics.di.retrofitModule
import com.goesplayer.music.di.musicModule
import com.goesplayer.player.client.MediaPlayerClient
import com.goestech.goesplayer.view.home.categorylist.CategoryListType
import com.goestech.goesplayer.view.home.categorylist.CategoryListViewModel
import com.goestech.goesplayer.view.home.categorylist.actions.AlbumListActions
import com.goestech.goesplayer.view.home.categorylist.actions.ArtistListActions
import com.goestech.goesplayer.view.home.categorylist.actions.CategoryListViewModelActions
import com.goestech.goesplayer.view.home.categorylist.actions.FolderListActions
import com.goestech.goesplayer.view.home.categorylist.actions.GenreListActions
import com.goestech.goesplayer.view.home.categorylist.actions.PlaylistListActions
import com.goestech.goesplayer.view.home.music.MusicViewModel
import com.goesplayer.player.client.MediaPlayerClientImpl
import com.goesplayer.player.di.playerModules
import com.goestech.goesplayer.view.player.screen.PlayerFragmentViewModel
import com.goestech.goesplayer.view.splash.SplashViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module


@FlowPreview
@ExperimentalCoroutinesApi
val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { MusicViewModel(get(), get(), get()) }
    viewModel { (type: CategoryListType) ->
        val action = getAll<CategoryListViewModelActions>().find { it.type == type }
            ?: throw IllegalStateException("$type not found")
        CategoryListViewModel(actions = action)
    }
    viewModel { PlayerFragmentViewModel(get(), get()) }
}

val actionsModule = module {
    factory { PlaylistListActions(get()) } bind CategoryListViewModelActions::class
    factory { ArtistListActions(get()) } bind CategoryListViewModelActions::class
    factory { AlbumListActions(get()) } bind CategoryListViewModelActions::class
    factory { GenreListActions(get()) } bind CategoryListViewModelActions::class
    factory { FolderListActions(get()) } bind CategoryListViewModelActions::class
}

@ExperimentalCoroutinesApi
@FlowPreview
val repositoryModule = module {
    factory { MediaPlayerClientImpl(androidContext()) }
}

@ExperimentalCoroutinesApi
@FlowPreview
val appModules = listOf(
    musicModule,
    playerModules,
    lyricsModule,
    retrofitModule,
    repositoryModule,
    actionsModule,
    viewModelModule
)