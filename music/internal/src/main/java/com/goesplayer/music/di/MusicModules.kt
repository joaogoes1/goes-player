package com.goesplayer.music.di

import com.goesplayer.music.data.datasource.music.DeviceStorageDataSource
import com.goesplayer.music.data.datasource.music.DeviceStorageDataSourceImpl
import com.goesplayer.music.data.datasource.music.MusicLocalDataSource
import com.goesplayer.music.data.datasource.music.MusicLocalDataSourceImpl
import com.goesplayer.music.data.datasource.playlist.PlaylistLocalDataSource
import com.goesplayer.music.data.datasource.playlist.PlaylistLocalDataSourceImpl
import com.goesplayer.music.data.repository.music.MusicRepository
import com.goesplayer.music.data.repository.music.MusicRepositoryImpl
import com.goesplayer.music.data.repository.playlist.PlaylistRepository
import com.goesplayer.music.data.repository.playlist.PlaylistRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val musicModule = module {
    factory<MusicRepository> { MusicRepositoryImpl(get(), get(), get()) }
    factory<PlaylistRepository> { PlaylistRepositoryImpl(get()) }
    single<DeviceStorageDataSource> { DeviceStorageDataSourceImpl(androidContext()) }
    single<MusicLocalDataSource> { MusicLocalDataSourceImpl() }
    single<PlaylistLocalDataSource> { PlaylistLocalDataSourceImpl() }
}