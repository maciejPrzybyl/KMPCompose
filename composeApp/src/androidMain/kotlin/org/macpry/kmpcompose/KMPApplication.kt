package org.macpry.kmpcompose

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.macpry.kmpcompose.di.appModule

class KMPApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@KMPApplication)
            modules(appModule())
        }
    }
}
