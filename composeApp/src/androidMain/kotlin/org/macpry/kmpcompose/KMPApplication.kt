package org.macpry.kmpcompose

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.macpry.kmpcompose.di.appModule
import org.macpry.kmpcompose.logger.KoinLogger

class KMPApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            logger(KoinLogger())
            androidContext(this@KMPApplication)
            modules(appModule())
        }
    }
}
