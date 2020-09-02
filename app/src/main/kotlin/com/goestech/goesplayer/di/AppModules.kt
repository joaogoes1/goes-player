package com.goestech.goesplayer.di

import com.goestech.goesplayer.bussiness.interactor.MusicInteractorImpl
import com.goestech.goesplayer.bussiness.interactor.MusicInteractor
import com.goestech.goesplayer.data.datasource.music.MusicDataSource
import com.goestech.goesplayer.data.datasource.music.MusicLocalDataSource
import com.goestech.goesplayer.data.datasource.music.MusicStorageDataSource
import com.goestech.goesplayer.data.datasource.music.MusicStorageDataSourceImpl
import com.goestech.goesplayer.data.repository.music.MusicRepository
import com.goestech.goesplayer.data.repository.music.MusicRepositoryImpl
import com.goestech.goesplayer.view.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
}

val interactorModule = module {
    factory<MusicInteractor> { MusicInteractorImpl(get()) }
}

val repositoryModule = module {
    factory<MusicRepository> { MusicRepositoryImpl(get(), get()) }
}

val dataSourceModule = module {
    single<MusicStorageDataSource> { MusicStorageDataSourceImpl(androidContext()) }
    single<MusicDataSource> { MusicLocalDataSource() }
}

val appModules = listOf(
    dataSourceModule,
    repositoryModule,
    interactorModule,
    viewModelModule
)