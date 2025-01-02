package org.macpry.kmpcompose.di

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.macpry.kmpcompose.services.worker.BackgroundWorker
import org.macpry.kmpcompose.services.worker.BackgroundWorker.Companion.NOTIFICATION_CONTENT
import org.macpry.kmpcompose.services.worker.BackgroundWorker.Companion.NOTIFICATION_TITLE
import platform.Foundation.NSUUID.Companion.UUID
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.time.Duration.Companion.seconds

actual val workersModule = module {
    singleOf(::IOSCountingWorker) bind BackgroundWorker::class
}

class IOSCountingWorker : BackgroundWorker() {
    override suspend fun start() {
        val uuidString = UUID().UUIDString
        range.forEach {
            triggerNotification(uuidString, it)
            delay(1.seconds)
        }
    }

    override val progressFlow: Flow<Int> = flowOf(10)
    override val tag: String = "CountingWorker"
}

private fun triggerNotification(uuidString: String, progress: Int) {
    val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
    notificationCenter.removePendingNotificationRequestsWithIdentifiers(listOf(uuidString))
    val notification = UNMutableNotificationContent()
    notification.setTitle(NOTIFICATION_TITLE)
    notification.setBody(NOTIFICATION_CONTENT + progress)
    val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(0.0001, false)
    val request = UNNotificationRequest.requestWithIdentifier(uuidString, notification, trigger)

    notificationCenter.addNotificationRequest(request) {
        println(it)
    }
}
