package org.macpry.kmpcompose

import cocoapods.FirebaseCore.FIRApp
import cocoapods.FirebaseMessaging.FIRMessaging
import kotlinx.cinterop.ExperimentalForeignApi
import org.macpry.kmpcompose.services.notifications.MessagingDelegate
import org.macpry.kmpcompose.services.notifications.NotificationsDelegate
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.UIKit.UIApplication
import platform.UIKit.registerForRemoteNotifications
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter

@OptIn(ExperimentalForeignApi::class)
class KotlinAppDelegate {

    fun didFinishLaunchingWithOptions(
        //application: UIApplication,
        //didFinishLaunchingWithOptions: Map<Any?, *>?
    ): Boolean {
        FIRApp.configure()
        val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
        notificationCenter.delegate = NotificationsDelegate()
        val authOptions =
            UNAuthorizationOptionAlert or UNAuthorizationOptionBadge or UNAuthorizationOptionSound
        notificationCenter.requestAuthorizationWithOptions(authOptions) { granted, error ->

        }
        UIApplication.sharedApplication().registerForRemoteNotifications()
        //application.registerForRemoteNotifications()
        FIRMessaging.messaging().delegate = MessagingDelegate()

        return true
    }

    /*FirebaseApp.configure()
    UNUserNotificationCenter.current().delegate = self
    let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
    UNUserNotificationCenter.current().requestAuthorization(
    options: authOptions,
    completionHandler: { _, _ in }
    )
    application.registerForRemoteNotifications()
    Messaging.messaging().delegate = self*/

    fun didRegisterForRemoteNotificationsWithDeviceToken(deviceToken: NSData) {
        FIRMessaging.messaging().setAPNSToken(deviceToken)
    }

    fun didFailToRegisterForRemoteNotificationsWithError(error: NSError) {
        println(error.localizedDescription)
    }
}
