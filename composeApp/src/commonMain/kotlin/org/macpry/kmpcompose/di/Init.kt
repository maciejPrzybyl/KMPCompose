package org.macpry.kmpcompose.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initKoin(doBefore: KoinApplication.() -> Unit = {}) {
    startKoin {
        doBefore()
        modules(appModule())
    }
}
