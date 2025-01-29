package org.macpry.kmpcompose.managers

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import kotlin.coroutines.suspendCoroutine
import platform.UIKit.UIApplication
import kotlin.coroutines.resume

actual class IdTokenProvider : IIdTokenProvider {
    actual override suspend fun getIdToken(): Result<String> = suspendCoroutine { continutation ->
        UIApplication.sharedApplication.keyWindow?.rootViewController?.let {

        } ?: continutation.resume(Result.failure(Exception("eeeeexxx")))
    }
}

actual fun idTokenModule() = module {
    factoryOf(::IdTokenProvider)
}