package com.goesplayer.di

import android.content.Context
import androidx.room.Room
import com.goesplayer.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun providesDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "goes-player-database"
    ).build()
}