package org.macpry.kmpcompose

import org.macpry.kmpcompose.services.notifications.NotificationsDelegate
import platform.UIKit.UIApplication
import platform.UIKit.registerForRemoteNotifications
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter

class KotlinAppDelegate {

    fun didFinishLaunchingWithOptions(
        //application: UIApplication,
        //didFinishLaunchingWithOptions: Map<Any?, *>?
    ): Boolean {
        //FirebaseApp.configure()
        val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
        notificationCenter.delegate = NotificationsDelegate()
        val authOptions = UNAuthorizationOptionAlert or UNAuthorizationOptionBadge or UNAuthorizationOptionSound
        notificationCenter.requestAuthorizationWithOptions(authOptions) { granted, error ->

        }
        UIApplication.sharedApplication().registerForRemoteNotifications()
        //application.registerForRemoteNotifications()
        //Messaging.messaging().delegate = self

        return true
    }
}
