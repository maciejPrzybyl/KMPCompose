package org.macpry.kmpcompose

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.macpry.kmpcompose.di.initKoin

class KMPApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@KMPApplication)
        }
    }
}
