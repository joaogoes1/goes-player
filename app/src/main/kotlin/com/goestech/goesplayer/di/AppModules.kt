package com.goestech.goesplayer.di

import androidx.room.Room
import com.goestech.goesplayer.data.database.AppDatabase
import com.goestech.goesplayer.data.datasource.music.DeviceStorageDataSource
import com.goestech.goesplayer.data.datasource.music.DeviceStorageDataSourceImpl
import com.goestech.goesplayer.data.datasource.music.MusicLocalDataSource
import com.goestech.goesplayer.data.datasource.music.MusicLocalDataSourceImpl
import com.goestech.goesplayer.data.datasource.playlist.PlaylistLocalDataSource
import com.goestech.goesplayer.data.datasource.playlist.PlaylistLocalDataSourceImpl
import com.goestech.goesplayer.data.repository.music.MusicRepository
import com.goestech.goesplayer.data.repository.music.MusicRepositoryImpl
import com.goestech.goesplayer.data.repository.playlist.PlaylistRepository
import com.goestech.goesplayer.data.repository.playlist.PlaylistRepositoryImpl
import com.goestech.goesplayer.view.home.artist.ArtistViewModel
import com.goestech.goesplayer.view.home.music.MusicViewModel
import com.goestech.goesplayer.view.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { MusicViewModel(get(), get()) }
    viewModel { ArtistViewModel(get()) }
}

val repositoryModule = module {
    factory<MusicRepository> { MusicRepositoryImpl(get(), get(), get()) }
    factory<PlaylistRepository> { PlaylistRepositoryImpl(get()) }
}

val dataSourceModule = module {
    single<DeviceStorageDataSource> { DeviceStorageDataSourceImpl(androidContext()) }
    single<MusicLocalDataSource> { MusicLocalDataSourceImpl(get()) }
    single<PlaylistLocalDataSource> { PlaylistLocalDataSourceImpl(get()) }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "AppDatabase"
        ).build()
    }
    single { get<AppDatabase>().musicDao() }
    single { get<AppDatabase>().playlistDao() }
}

val appModules = listOf(
    databaseModule,
    dataSourceModule,
    repositoryModule,
    viewModelModule
)