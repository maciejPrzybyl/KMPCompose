import SwiftUI
import ComposeApp
import UserNotifications

import Firebase
import FirebaseMessaging
import GoogleSignIn
import GoogleSignInSwift

class AppDelegate: NSObject, UIApplicationDelegate {

  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {

    FirebaseApp.configure()
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

  func application(
    _ application: UIApplication,
    didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
  ) {
    Messaging.messaging().apnsToken = deviceToken
  }

  func application(
      _ application: UIApplication,
      didFailToRegisterForRemoteNotificationsWithError error: any Error
  ) {
      NSLog("aaaa didFailToRegisterForRemoteNotificationsWithError error: \(error.localizedDescription)")
  }

  func application(_ app: UIApplication,
                     open url: URL,
                     options: [UIApplication.OpenURLOptionsKey: Any] = [:]) -> Bool {
      // [END application_open]
      if GIDSignIn.sharedInstance.handle(url) {
        return true
      } else {
        return false
      }
      /*return ApplicationDelegate.shared.application(
        app,
        open: url,
        sourceApplication: options[UIApplication.OpenURLOptionsKey.sourceApplication] as? String,
        annotation: options[UIApplication.OpenURLOptionsKey.annotation]
      )*/
    }
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

class GoogleSign {
    
    func signInWithGoogle(rootViewController: UIViewController) {
        //GoogleSignInButton(style: .wide) {
            /*guard let rootViewController = self.rootViewController else {
                print("No root view controller")
                return
            }*/
        GIDSignIn.sharedInstance.signIn(withPresenting: rootViewController) { result, error in
          guard let result else {
            print("Error signing in: \(String(describing: error))")
            return
          }
          print("Successfully signed in user")
        }
        //}
    }
}
