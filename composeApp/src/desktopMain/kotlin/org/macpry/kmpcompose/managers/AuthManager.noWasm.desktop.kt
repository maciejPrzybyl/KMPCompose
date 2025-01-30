package org.macpry.kmpcompose.managers

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual class TokenProvider : ITokenProvider {
    actual override suspend fun getToken(): Result<Token> {
        TODO("Not yet implemented")
    }
}

actual fun tokenModule() = module {
    factoryOf(::TokenProvider)
}