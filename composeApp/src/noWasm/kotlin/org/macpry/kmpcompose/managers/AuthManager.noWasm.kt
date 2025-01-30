package org.macpry.kmpcompose.managers

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.macpry.kmpcompose.repositories.CurrentUser

expect class TokenProvider : ITokenProvider {
    override suspend fun getToken(): Result<Token>
}

expect fun tokenModule(): Module

actual val authModule = module {
    includes(tokenModule())
    factoryOf(::AuthManager) bind IAuthManager::class
}

actual class AuthManager(private val tokenProvider: TokenProvider) : IAuthManager {
    actual override suspend fun signIn() = tokenProvider.getToken().mapCatching {
        Firebase.auth.signInWithCredential(
            GoogleAuthProvider.credential(idToken = it.idToken, accessToken = it.accessToken)
        ).user?.let {
            CurrentUser(it.displayName.orEmpty(), it.email.orEmpty(), it.photoURL)
        } ?: throw Exception("No user found")
    }
}
