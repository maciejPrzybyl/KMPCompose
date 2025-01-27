@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package org.macpry.kmpcompose.managers

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.macpry.kmpcompose.repositories.CurrentUser

actual val authModule = module {
    factoryOf(::AuthManager) bind IAuthManager::class
}

actual class AuthManager : IAuthManager {
    actual override suspend fun signIn(): Result<CurrentUser> {
        TODO("Not yet implemented")
    }
}
