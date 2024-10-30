package org.macpry.kmpcompose.di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import kotlin.test.Test

class AppModulesTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAppModule() {
        appModule().verify(
            extraTypes = listOf(
                io.ktor.client.engine.HttpClientEngine::class,
                io.ktor.client.HttpClientConfig::class
            )
        )
    }
}