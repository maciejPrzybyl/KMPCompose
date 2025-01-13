@file:OptIn(ExperimentalForeignApi::class)

package org.macpry.kmpcompose.services.notifications

import cocoapods.FirebaseMessaging.FIRMessaging
import cocoapods.FirebaseMessaging.FIRMessagingDelegateProtocol
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNotificationCenter
import platform.darwin.NSObject

class MessagingDelegate : NSObject(), FIRMessagingDelegateProtocol {

    override fun messaging(messaging: FIRMessaging, didReceiveRegistrationToken: String?) {
        val tokenDict = mapOf<Any?, String>("token" to didReceiveRegistrationToken.orEmpty())
        NSNotificationCenter.defaultCenter.postNotificationName(
            "FCMToken", null, tokenDict
        )
    }

}