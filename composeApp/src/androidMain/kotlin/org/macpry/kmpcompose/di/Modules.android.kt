package org.macpry.kmpcompose.di

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.macpry.kmpcompose.providers.KMPDispatchers
import org.macpry.kmpcompose.services.worker.BackgroundWorker
import kotlin.time.Duration.Companion.seconds

actual val workersModule = module {
    singleOf(::AndroidBackgroundWorker) bind BackgroundWorker::class
    single { WorkManager.getInstance(get()) }
    worker { CountingWorker(get(), get(), get(named(KMPDispatchers.IO))) }
}

class AndroidBackgroundWorker(
    private val workManager: WorkManager
) : BackgroundWorker {
    override fun start() {
        workManager.enqueue(OneTimeWorkRequestBuilder<CountingWorker>().build())
    }
}

class CountingWorker(
    appContext: Context,
    params: WorkerParameters,
    private val ioDispatcher: CoroutineDispatcher
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        (1..10).forEach {
            println("BackgroundWorker: $it")
            delay(1.seconds)
        }
        Result.success()
    }
}
