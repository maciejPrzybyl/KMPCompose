package org.macpry.kmpcompose.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module

fun koinConfiguration(): KoinAppDeclaration = {
    modules(AppModule().module)
}

@Module(includes = [ManagersModule::class, NetworkModule::class, ViewModelsModule::class])
class AppModule

@Module
@ComponentScan("org.macpry.kmpcompose.managers")
class ManagersModule

@Module
@ComponentScan("org.macpry.kmpcompose.network")
class NetworkModule {

    @Single
    fun provideHttpClient() = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
}

@Module
@ComponentScan("org.macpry.kmpcompose.screens")
class ViewModelsModule
