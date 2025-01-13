import SwiftUI
import ComposeApp
import UserNotifications

class IOSAppDelegate: NSObject, UIApplicationDelegate {

    private let kotlinAppDelegate = KotlinAppDelegate()

    func application(_ application: UIApplication,
                       didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
                       return kotlinAppDelegate.didFinishLaunchingWithOptions(/*application, launchOptions*/)
    }

    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
      ) {
            kotlinAppDelegate.didRegisterForRemoteNotificationsWithDeviceToken(deviceToken)
        }

      func application(
          _ application: UIApplication,
          didFailToRegisterForRemoteNotificationsWithError error: any Error
      ) {
          kotlinAppDelegate.didFailToRegisterForRemoteNotificationsWithError(error)
      }
}


@main
struct iOSApp: App {
    init() {
        InitKt.doInitKoin()
    }
    @UIApplicationDelegateAdaptor(IOSAppDelegate.self) var appDelegate
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
