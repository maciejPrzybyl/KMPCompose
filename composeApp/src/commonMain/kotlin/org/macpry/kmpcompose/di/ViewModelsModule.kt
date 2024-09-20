package org.macpry.kmpcompose.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.macpry.kmpcompose.AppViewModel

val viewModelsModule = module {
    viewModelOf(::AppViewModel)
}