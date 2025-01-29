package org.macpry.kmpcompose.managers

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual class IdTokenProvider : IIdTokenProvider {
    actual override suspend fun getIdToken(): Result<String> {
        TODO("Not yet implemented")
    }
}

actual fun idTokenModule() = module {
    factoryOf(::IdTokenProvider)
}