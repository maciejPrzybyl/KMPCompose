package org.macpry.kmpcompose.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.macpry.kmpcompose.providers.KMPDispatchers
import org.macpry.kmpcompose.services.backgroundworker.IOSCountingWorker
import org.macpry.kmpcompose.services.worker.BackgroundWorker

actual val workersModule = module {
    single<BackgroundWorker> { IOSCountingWorker(get(named(KMPDispatchers.IO))) }
}
