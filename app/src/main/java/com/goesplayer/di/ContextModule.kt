package com.goesplayer.di

import android.content.ContentResolver
import com.goesplayer.GoesPlayerApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ContentResolverModule {

    @Provides
    fun providesContentResolver(
        application: GoesPlayerApplication,
    ): ContentResolver = application.contentResolver
}