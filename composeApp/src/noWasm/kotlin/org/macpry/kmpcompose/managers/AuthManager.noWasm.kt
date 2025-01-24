package org.macpry.kmpcompose.managers

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect class TokenIdProvider() : ITokenIdProvider {
    override suspend fun getTokenId(): String
}

actual val authModule = module {
    factoryOf(::TokenIdProvider) bind ITokenIdProvider::class
    factoryOf(::AuthManager) bind IAuthManager::class
}

actual class AuthManager actual constructor(private val tokenIdProvider: ITokenIdProvider) : IAuthManager {
    actual override suspend fun signIn() {

    }
}
