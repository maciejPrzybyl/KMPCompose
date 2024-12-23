package org.macpry.kmpcompose.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
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
        workManager.enqueueUniqueWork(
            tag,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<CountingWorker>()
                .addTag(tag)
                .build()
        )
    }

    override val tag: String = "CountingWorker"

    override val progressFlow: Flow<Int> = workManager.getWorkInfosByTagFlow(tag).map {
        it.firstOrNull()?.let { workInfo ->
            if (workInfo.state == WorkInfo.State.RUNNING) {
                workInfo.progress.getInt(PROGRESS_TAG, 0)
            } else 0
        } ?: 0
    }
}

class CountingWorker(
    appContext: Context,
    params: WorkerParameters,
    private val ioDispatcher: CoroutineDispatcher
) : CoroutineWorker(appContext, params) {

    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val notificationBuilder =
        NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
            val title = "Count"
            val cancel = "Cancel"
            val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)
            setContentTitle(title)
            setTicker(title)
            setContentText("Counting progress")
            setProgress(MAX_PROGRESS, 0, false)
            setSmallIcon(android.R.drawable.btn_star)
            setOngoing(true)
            setOnlyAlertOnce(true)
            addAction(android.R.drawable.ic_delete, cancel, intent)
        }

    private fun updateNotification(progress: Int) {
        val updatedNotification =
            notificationBuilder.setProgress(MAX_PROGRESS, progress, false).build()
        notificationManager.notify(NOTIFICATION_ID, updatedNotification)
    }

    private fun createForegroundInfo(): ForegroundInfo {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
        return ForegroundInfo(
            NOTIFICATION_ID,
            notificationBuilder.build(),
            FOREGROUND_SERVICE_TYPE_SHORT_SERVICE
        )
    }

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        setForeground(createForegroundInfo())
        try {
            (0..MAX_PROGRESS).step(10).forEach {
                updateNotification(it)
                setProgress(workDataOf(BackgroundWorker.PROGRESS_TAG to it))
                delay(1.seconds)
            }
            Result.success()
        } catch (exception: Exception) {
            Result.failure()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val name = "channel_name"
        val descriptionText = "channel_description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = descriptionText
        notificationManager.createNotificationChannel(mChannel)
    }

    companion object {
        const val CHANNEL_ID = "CountingWorker_notification_channel_id"
        const val NOTIFICATION_ID = 987123
        const val MAX_PROGRESS = 100
    }
}
