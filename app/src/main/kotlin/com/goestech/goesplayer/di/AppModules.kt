package com.goestech.goesplayer.di

import androidx.room.Room
import com.goestech.goesplayer.data.database.AppDatabase
import com.goestech.goesplayer.data.database.dao.MusicDao
import com.goestech.goesplayer.data.datasource.music.MusicLocalDataSource
import com.goestech.goesplayer.data.datasource.music.MusicLocalDataSourceImpl
import com.goestech.goesplayer.data.datasource.music.MusicStorageDataSource
import com.goestech.goesplayer.data.datasource.music.MusicStorageDataSourceImpl
import com.goestech.goesplayer.data.repository.music.MusicRepository
import com.goestech.goesplayer.data.repository.music.MusicRepositoryImpl
import com.goestech.goesplayer.view.home.artist.ArtistViewModel
import com.goestech.goesplayer.view.home.music.MusicViewModel
import com.goestech.goesplayer.view.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { MusicViewModel(get()) }
    viewModel { ArtistViewModel(get()) }
}

val repositoryModule = module {
    factory<MusicRepository> { MusicRepositoryImpl(get(), get()) }
}

val dataSourceModule = module {
    single<MusicStorageDataSource> { MusicStorageDataSourceImpl(androidContext()) }
    single<MusicLocalDataSource> { MusicLocalDataSourceImpl(get()) }
}

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "AppDatabase"
        ).build()
    }
    single<MusicDao> { get<AppDatabase>().musicDao() }
}

val appModules = listOf(
    databaseModule,
    dataSourceModule,
    repositoryModule,
    viewModelModule
)