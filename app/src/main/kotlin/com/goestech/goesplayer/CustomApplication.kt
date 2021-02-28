package com.goestech.goesplayer

import android.app.Application
import com.goestech.goesplayer.di.appModules
import io.realm.Realm
import io.realm.RealmConfiguration
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
        initKoin()
        initRealm()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@CustomApplication)
            modules(appModules)
        }
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration
            .Builder()
            .build()
        Realm.setDefaultConfiguration(config)
    }
}