package org.macpry.kmpcompose.managers

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.macpry.kmpcompose.repositories.CurrentUser

expect class IdTokenProvider : IIdTokenProvider {
    override suspend fun getIdToken(): Result<String>
}

expect fun idTokenModule(): Module

actual val authModule = module {
    includes(idTokenModule())
    factoryOf(::AuthManager) bind IAuthManager::class
}

actual class AuthManager(private val idTokenProvider: IdTokenProvider) : IAuthManager {
    actual override suspend fun signIn() = idTokenProvider.getIdToken().mapCatching {
        Firebase.auth.signInWithCredential(
            GoogleAuthProvider.credential(idToken = it, accessToken = null)
        ).user?.let {
            CurrentUser(it.displayName.orEmpty(), it.email.orEmpty(), it.photoURL)
        } ?: throw Exception("No user found")
    }
}
