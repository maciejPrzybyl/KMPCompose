package org.macpry.kmpcompose.managers

import org.koin.core.module.Module

expect val authModule: Module

interface ITokenIdProvider {
    suspend fun getTokenId(): String
}

interface IAuthManager {
    suspend fun signIn()
}

expect class AuthManager(tokenIdProvider: ITokenIdProvider) : IAuthManager {
    override suspend fun signIn()
}
