package org.macpry.kmpcompose.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.macpry.kmpcompose.AppViewModel
import org.macpry.kmpcompose.AppManager
import org.macpry.kmpcompose.TimeProvider

fun koinConfiguration() = koinApplication {
    modules(appModule, viewModelsModule)
}

val appModule = module {
    singleOf(::AppManager)
    singleOf(::TimeProvider)
}

val viewModelsModule = module {
    viewModelOf(::AppViewModel)
}
