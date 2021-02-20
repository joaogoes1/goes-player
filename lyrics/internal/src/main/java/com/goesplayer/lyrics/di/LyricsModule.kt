package com.goesplayer.lyrics.di

import com.goesplayer.lyrics.data.api.LyricsApi
import com.goesplayer.lyrics.data.datasource.lyrics.LyricsRemoteDataSource
import com.goesplayer.lyrics.data.datasource.lyrics.LyricsRemoteDataSourceImpl
import com.goestech.lyrics.data.repository.LyricsRepository
import com.goesplayer.lyrics.data.repository.LyricsRepositoryImpl
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private const val VAGALUME_BASE_PATH = "https://api.vagalume.com.br/"

val lyricsModule = module {
    factory<com.goestech.lyrics.data.repository.LyricsRepository> { LyricsRepositoryImpl(get()) }
    single<LyricsRemoteDataSource> { LyricsRemoteDataSourceImpl(get()) }
}




val retrofitModule = module {
    val retrofit = Retrofit.Builder()
        .baseUrl(VAGALUME_BASE_PATH)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    single<LyricsApi> { retrofit.create(LyricsApi::class.java) }
}