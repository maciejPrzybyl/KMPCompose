package org.macpry.kmpcompose.services.backgroundworker

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.macpry.kmpcompose.services.worker.BackgroundWorker
import org.macpry.kmpcompose.services.worker.count
import platform.Foundation.NSUUID.Companion.UUID
import platform.UIKit.UIApplication
import platform.UIKit.UIBackgroundTaskIdentifier
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter

class IOSCountingWorker(
    private val ioDispatcher: CoroutineDispatcher
) : BackgroundWorker() {

    private val progress = MutableStateFlow(0)

    override fun start() {
        val uuidString = UUID().UUIDString

        var identifier: UIBackgroundTaskIdentifier? = null
        identifier = UIApplication.sharedApplication.beginBackgroundTaskWithName(tag) {
            identifier?.let { UIApplication.sharedApplication.endBackgroundTask(it) }
        }
        CoroutineScope(ioDispatcher).launch {
            count(
                ioDispatcher,
                onInit = {},
                onEach = {
                    triggerNotification(uuidString, it)
                    progress.emit(it)
                }
            ).onSuccess {
                UIApplication.sharedApplication.endBackgroundTask(identifier)
            }.onFailure {
                UIApplication.sharedApplication.endBackgroundTask(identifier)
            }
        }
    }

    override val progressFlow: Flow<Int> = progress
    override val tag: String = "CountingWorker"
}

private fun triggerNotification(uuidString: String, progress: Int) {
    val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
    notificationCenter.removePendingNotificationRequestsWithIdentifiers(listOf(uuidString))
    val notification = UNMutableNotificationContent()
    notification.setTitle(BackgroundWorker.NOTIFICATION_TITLE)
    notification.setBody(BackgroundWorker.NOTIFICATION_CONTENT + progress)
    val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(0.0001, false)
    val request = UNNotificationRequest.requestWithIdentifier(uuidString, notification, trigger)

    notificationCenter.addNotificationRequest(request) {
        println(it)
    }
}