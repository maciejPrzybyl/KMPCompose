package org.macpry.kmpcompose.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    singleOf(::AndroidCountingWorker) bind BackgroundWorker::class
    single { WorkManager.getInstance(get()) }
    worker { CountingWorker(get(), get(), get(named(KMPDispatchers.IO))) }
}

class AndroidCountingWorker(
    private val workManager: WorkManager
) : BackgroundWorker() {

    override fun start() {
        workManager.enqueue(
            OneTimeWorkRequestBuilder<CountingWorker>()
                .addTag(tag)
                .build()
        )
    }

    override val tag: String = "CountingWorker"

    override val progressFlow: Flow<Int> = workManager.getWorkInfosByTagFlow(tag).map {
        it.firstOrNull()?.progress?.getInt(PROGRESS, 0) ?: 0
    }
}

class CountingWorker(
    appContext: Context,
    params: WorkerParameters,
    private val ioDispatcher: CoroutineDispatcher
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        setForeground(createForegroundInfo(0))
        (1..100).step(10).forEach {
            setForeground(createForegroundInfo(it))
            setProgress(workDataOf(BackgroundWorker.PROGRESS to it))
            delay(1.seconds)
        }
        Result.success()
    }

    private fun createForegroundInfo(progress: Int): ForegroundInfo {
        val title = "Count"
        val cancel = "Cancel"
        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText("Counting progress")
            .setProgress(100, progress, false)
            .setSmallIcon(android.R.drawable.btn_star)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()

        return ForegroundInfo(NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_SHORT_SERVICE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val name = "channel_name"
        val descriptionText = "channel_description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = descriptionText
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    companion object {
        const val CHANNEL_ID = "CountingWorker_notification_channel_id"
        const val NOTIFICATION_ID = 987123
    }
}
