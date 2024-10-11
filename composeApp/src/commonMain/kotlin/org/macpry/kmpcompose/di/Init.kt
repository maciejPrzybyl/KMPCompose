package org.macpry.kmpcompose.di

import org.koin.core.context.startKoin
import org.macpry.kmpcompose.logger.KoinLogger

fun initKoin() {
    startKoin {
        logger(KoinLogger())
        modules(appModule())
    }
}
