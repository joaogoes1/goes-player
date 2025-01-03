package com.goesplayer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class GoesPlayerApplication @Inject constructor() : Application() {
}