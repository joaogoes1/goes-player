package com.goestech.goesplayer.di

import androidx.room.Room
import com.goestech.goesplayer.data.api.LyricsApi
import com.goestech.goesplayer.data.database.AppDatabase
import com.goestech.goesplayer.data.datasource.lyrics.LyricsRemoteDataSource
import com.goestech.goesplayer.data.datasource.lyrics.LyricsRemoteDataSourceImpl
import com.goestech.goesplayer.data.datasource.music.DeviceStorageDataSource
import com.goestech.goesplayer.data.datasource.music.DeviceStorageDataSourceImpl
import com.goestech.goesplayer.data.datasource.music.MusicLocalDataSource
import com.goestech.goesplayer.data.datasource.music.MusicLocalDataSourceImpl
import com.goestech.goesplayer.data.datasource.playlist.PlaylistLocalDataSource
import com.goestech.goesplayer.data.datasource.playlist.PlaylistLocalDataSourceImpl
import com.goestech.goesplayer.data.repository.lyrics.LyricsRepository
import com.goestech.goesplayer.data.repository.lyrics.LyricsRepositoryImpl
import com.goestech.goesplayer.data.repository.music.MusicRepository
import com.goestech.goesplayer.data.repository.music.MusicRepositoryImpl
import com.goestech.goesplayer.data.repository.playlist.PlaylistRepository
import com.goestech.goesplayer.data.repository.playlist.PlaylistRepositoryImpl
import com.goestech.goesplayer.view.home.artist.ArtistViewModel
import com.goestech.goesplayer.view.home.music.MusicViewModel
import com.goestech.goesplayer.view.player.MediaPlayerClient
import com.goestech.goesplayer.view.player.screen.PlayerFragmentViewModel
import com.goestech.goesplayer.view.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val VAGALUME_BASE_PATH = "https://api.vagalume.com.br/"

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { MusicViewModel(get(), get(), get()) }
    viewModel { ArtistViewModel(get()) }
    viewModel { PlayerFragmentViewModel(get(), get()) }
}

val repositoryModule = module {
    factory { MediaPlayerClient(androidContext()) }
    factory<MusicRepository> { MusicRepositoryImpl(get(), get(), get()) }
    factory<PlaylistRepository> { PlaylistRepositoryImpl(get()) }
    factory<LyricsRepository> { LyricsRepositoryImpl(get()) }
}

val dataSourceModule = module {
    single<DeviceStorageDataSource> { DeviceStorageDataSourceImpl(androidContext()) }
    single<MusicLocalDataSource> { MusicLocalDataSourceImpl(get()) }
    single<PlaylistLocalDataSource> { PlaylistLocalDataSourceImpl(get()) }
    single<LyricsRemoteDataSource> { LyricsRemoteDataSourceImpl(get()) }
}

val retrofitModule = module {
    val retrofit = Retrofit.Builder()
            .baseUrl(VAGALUME_BASE_PATH)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    single<LyricsApi> { retrofit.create(LyricsApi::class.java) }
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
    retrofitModule,
    databaseModule,
    dataSourceModule,
    repositoryModule,
    viewModelModule
)