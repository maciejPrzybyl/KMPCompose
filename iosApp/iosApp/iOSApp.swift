import SwiftUI
import ComposeApp
import UserNotifications

import Firebase
import FirebaseMessaging

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {

    FirebaseApp.configure()
    //FirebaseConfiguration.shared.setLoggerLevel(.min)
    UNUserNotificationCenter.current().delegate = self
    let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
    UNUserNotificationCenter.current().requestAuthorization(
        options: authOptions,
        completionHandler: { _, _ in }
    )
    application.registerForRemoteNotifications()
    Messaging.messaging().delegate = self

    return true
  }
}

func application(
  _ application: UIApplication,
  didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
) {
  Messaging.messaging().apnsToken = deviceToken
  if  let str = String(data: deviceToken, encoding: .utf8) { NSLog("aaaa didRegisterForRemoteNotificationsWithDeviceToken deviceToken: \(str)") }
}

func application(
    _ application: UIApplication,
    didFailToRegisterForRemoteNotificationsWithError error: any Error
) {
    NSLog("aaaa didFailToRegisterForRemoteNotificationsWithError error: \(error.localizedDescription)")
}

extension AppDelegate: UNUserNotificationCenterDelegate {
  func userNotificationCenter(
    _ center: UNUserNotificationCenter,
    willPresent notification: UNNotification,
    withCompletionHandler completionHandler:
    @escaping (UNNotificationPresentationOptions) -> Void
  ) {
    completionHandler([[.banner, .sound]])
  }

  func userNotificationCenter(
    _ center: UNUserNotificationCenter,
    didReceive response: UNNotificationResponse,
    withCompletionHandler completionHandler: @escaping () -> Void
  ) {
    completionHandler()
  }
}

extension AppDelegate: MessagingDelegate {
  func messaging(
    _ messaging: Messaging,
    didReceiveRegistrationToken fcmToken: String?
  ) {
    let tokenDict = ["token": fcmToken ?? ""]
    NotificationCenter.default.post(
      name: Notification.Name("FCMToken"),
      object: nil,
      userInfo: tokenDict
    )
    NSLog("aaaa didReceiveRegistrationToken fcmToken: " + (fcmToken ?? "Not found"))
  }
}


@main
struct iOSApp: App {
    init() {
        InitKt.doInitKoin()
    }
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
