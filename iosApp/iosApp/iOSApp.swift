import SwiftUI
import ComposeApp
import UserNotifications

import Firebase
import FirebaseMessaging

import BackgroundTasks

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
        
        BGTaskScheduler.shared.register(forTaskWithIdentifier: "org.macpry.kmpcompose.BackgroundWorker", using: nil) { task in
             self.handleAppRefresh(task: task as! BGAppRefreshTask)
        }
        
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
    
    func scheduleAppRefresh() {
       let request = BGAppRefreshTaskRequest(identifier: "org.macpry.kmpcompose.BackgroundWorker")
       request.earliestBeginDate = Date(timeIntervalSinceNow: 0)
            
       do {
          try BGTaskScheduler.shared.submit(request)
       } catch {
          print("Could not schedule app refresh: \(error)")
       }
    }
    
    func handleAppRefresh(task: BGAppRefreshTask) {
       // Schedule a new refresh task.
       scheduleAppRefresh()
        triggerNotifcation()


       // Create an operation that performs the main part of the background task.
       /*let operation = RefreshAppContentsOperation()
       
       // Provide the background task with an expiration handler that cancels the operation.
       task.expirationHandler = {
          operation.cancel()
       }


       // Inform the system that the background task is complete
       // when the operation completes.
       operation.completionBlock = {
          task.setTaskCompleted(success: !operation.isCancelled)
       }


       // Start the operation.
       operationQueue.addOperation(operation)*/
     }
}

func triggerNotifcation() {
    let content = UNMutableNotificationContent()
    content.title = "Count"
    content.body = "Counting progress"
    let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 0, repeats: false)
    let uuidString = UUID().uuidString
    let request = UNNotificationRequest(identifier: uuidString, content: content, trigger: trigger)


    // Schedule the request with the system.
    let notificationCenter = UNUserNotificationCenter.current()
    do {
        try notificationCenter.add(request)
    } catch {
        // Handle errors that may occur during add.
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
