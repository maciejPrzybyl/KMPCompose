package org.macpry.kmpcompose.di

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.macpry.kmpcompose.services.worker.BackgroundWorker
import org.macpry.kmpcompose.services.worker.DummyBackgroundWorker

actual val workersModule = module {
    singleOf(::DummyBackgroundWorker) bind BackgroundWorker::class
}

class IOSCountingWorker: BackgroundWorker() {
    override fun start() {
        TODO("Not yet implemented")
    }

    override val progressFlow: Flow<Int> = callbackFlow {
        awaitClose {  }
    }
    override val tag: String = "CountingWorker"
}
