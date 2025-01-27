package org.macpry.kmpcompose.managers

import org.koin.core.module.Module

actual class IdTokenProvider : IIdTokenProvider {
    actual override suspend fun getIdToken(): Result<String> {
        TODO("Not yet implemented")
    }
}

actual fun idTokenModule(): Module {
    TODO("Not yet implemented")
}