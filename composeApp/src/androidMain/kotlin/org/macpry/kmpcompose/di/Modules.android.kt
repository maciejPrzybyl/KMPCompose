package org.macpry.kmpcompose.di

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.binds
import org.koin.dsl.module
import org.macpry.kmpcompose.services.worker.BackgroundWorker
import kotlin.time.Duration.Companion.seconds

actual val workersModule = module {
    //worker { AndroidBackgroundWorker(get(), get()) } bind BackgroundWorker::class
    workerOf(::AndroidBackgroundWorker) binds arrayOf(BackgroundWorker::class)
}

class AndroidBackgroundWorker(
    private val appContext: Context,
    params: WorkerParameters,
    private val ioDispatcher: CoroutineDispatcher
) : BackgroundWorker, CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        (1..10).forEach {
            println("BackgroundWorker: $it")
            delay(1.seconds)
        }
        Result.success()
    }

    override fun onProgressChanged() {
        TODO("Not yet implemented")
    }

    companion object {
        fun start() = OneTimeWorkRequestBuilder<AndroidBackgroundWorker>().build()
    }
}
