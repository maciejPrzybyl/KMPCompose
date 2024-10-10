import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        InitKt.doInitKoin()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}