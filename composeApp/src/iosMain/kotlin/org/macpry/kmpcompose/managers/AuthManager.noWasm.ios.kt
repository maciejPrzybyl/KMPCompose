package org.macpry.kmpcompose.managers

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume

actual class TokenProvider : ITokenProvider {
    actual override suspend fun getToken(): Result<Token> = suspendCoroutine { continuation ->
        continuation.resume(Result.success(Token("123", "456")))
        /*UIApplication.sharedApplication.keyWindow?.rootViewController?.let {

        } ?: continuation.resume(Result.failure(Exception("eeeeexxx")))*/
    }
}

actual fun tokenModule() = module {
    factoryOf(::TokenProvider)
}