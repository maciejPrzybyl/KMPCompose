package org.macpry.kmpcompose.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.macpry.kmpcompose.services.worker.BackgroundWorker
import org.macpry.kmpcompose.services.worker.DummyBackgroundWorker

actual val workersModule = module {
    singleOf(::DummyBackgroundWorker) bind BackgroundWorker::class
}