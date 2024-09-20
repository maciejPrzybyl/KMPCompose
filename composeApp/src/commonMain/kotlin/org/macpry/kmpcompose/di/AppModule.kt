package org.macpry.kmpcompose.di

import org.koin.dsl.koinApplication

fun koinConfiguration() = koinApplication {
    modules(viewModelsModule)
}
