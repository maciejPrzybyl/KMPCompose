package org.macpry.kmpcompose.managers

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.macpry.kmpcompose.BuildConfig

actual class TokenProvider(
    private val context: Context,
    private val credentialManager: CredentialManager
) : ITokenProvider {
    actual override suspend fun getToken(): Result<Token> {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .setServerClientId(BuildConfig.SERVER_CLIENT_ID)
            .build()
        val credentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        return runCatching {
            val credential = credentialManager.getCredential(context, credentialRequest).credential
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                Token(GoogleIdTokenCredential.createFrom(credential.data).idToken, null)
            } else return Result.failure(Exception("Unexpected type of credential"))
        }
    }
}

actual fun tokenModule() = module {
    factory { CredentialManager.create(get()) }
    factoryOf(::TokenProvider)
}