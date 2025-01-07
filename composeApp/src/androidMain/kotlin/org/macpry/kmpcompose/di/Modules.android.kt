package org.macpry.kmpcompose.di

import androidx.work.WorkManager
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.macpry.kmpcompose.providers.KMPDispatchers
import org.macpry.kmpcompose.services.backgroundworker.CountingWorker
import org.macpry.kmpcompose.services.backgroundworker.AndroidCountingWorker
import org.macpry.kmpcompose.services.worker.BackgroundWorker

actual val workersModule = module {
    singleOf(::CountingWorker) bind BackgroundWorker::class
    single { WorkManager.getInstance(get()) }
    worker { AndroidCountingWorker(get(), get(), get(named(KMPDispatchers.IO))) }
}
