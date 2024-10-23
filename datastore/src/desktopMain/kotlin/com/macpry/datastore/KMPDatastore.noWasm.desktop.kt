package com.macpry.datastore

import org.koin.dsl.module

actual fun dataStorePath() = module {
    single { dataStoreFileName }
}