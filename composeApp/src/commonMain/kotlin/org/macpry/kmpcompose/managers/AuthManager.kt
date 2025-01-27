package org.macpry.kmpcompose.managers

import org.koin.core.module.Module
import org.macpry.kmpcompose.repositories.CurrentUser

expect val authModule: Module

interface IAuthManager {
    suspend fun signIn(): Result<CurrentUser>
}

interface IIdTokenProvider {
    suspend fun getIdToken(): Result<String>
}

expect class AuthManager : IAuthManager {
    override suspend fun signIn(): Result<CurrentUser>
}
