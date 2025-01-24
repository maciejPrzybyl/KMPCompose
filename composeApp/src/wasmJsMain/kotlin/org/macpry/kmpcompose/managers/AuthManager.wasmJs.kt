@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package org.macpry.kmpcompose.managers

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val authModule = module {
    factoryOf(::AuthManager) bind IAuthManager::class
}

actual class AuthManager actual constructor(tokenIdProvider: ITokenIdProvider) : IAuthManager {
    actual override suspend fun signIn() {
    }
}
