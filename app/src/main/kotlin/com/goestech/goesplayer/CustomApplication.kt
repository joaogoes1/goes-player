package com.goesplayer

import android.app.Application
import com.goesplayer.di.appModules
import io.realm.Realm
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@FlowPreview
@ExperimentalCoroutinesApi
class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        startKoin {
            androidContext(this@CustomApplication)
            modules(appModules)
        }
    }
}