package com.goesplayer.player.di

import com.goesplayer.player.client.MediaPlayerClient
import com.goesplayer.player.client.MediaPlayerClientImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val playerModules = module {
    single<MediaPlayerClient> { MediaPlayerClientImpl(androidContext()) }
}