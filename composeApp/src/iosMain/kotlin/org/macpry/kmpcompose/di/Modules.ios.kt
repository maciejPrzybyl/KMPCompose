package org.macpry.kmpcompose.di

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.macpry.kmpcompose.services.worker.BackgroundWorker
import org.macpry.kmpcompose.services.worker.count
import platform.Foundation.NSUUID.Companion.UUID
import platform.UIKit.UIBackgroundTaskIdentifier
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.time.Duration.Companion.seconds

actual val workersModule = module {
    singleOf(::IOSCountingWorker) bind BackgroundWorker::class
}

class IOSCountingWorker : BackgroundWorker() {
    override fun start() {
        triggerNotification()
    }

    override val progressFlow: Flow<Int> = flowOf(10)
    override val tag: String = "CountingWorker"
}

private suspend fun initWork() {
    count(onEach = {}, onSuccess = {}, onFailure = {})
}

private fun triggerNotification() {
    val notification = UNMutableNotificationContent()
    notification.setTitle("Count")
    notification.setBody("Counting progress")
    val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(1.0, false)
    val uuidString = UUID().UUIDString
    val request = UNNotificationRequest.requestWithIdentifier(uuidString, notification, trigger)

    UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request) {
        println(it)
    }
}