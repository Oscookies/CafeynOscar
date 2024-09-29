package com.oscorella.cafeyn

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CafeynApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}