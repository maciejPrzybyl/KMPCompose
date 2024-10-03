package org.macpry.kmpcompose.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module

fun koinConfiguration(): KoinAppDeclaration = {
    modules(AppModule().module)
}

@Module(includes = [DataModule::class, ProvidersModule::class, ManagersModule::class, ViewModelsModule::class])
class AppModule

@Module
@ComponentScan("org.macpry.kmpcompose.data")
class DataModule

@Module
@ComponentScan("org.macpry.kmpcompose.managers")
class ManagersModule

@Module
@ComponentScan("org.macpry.kmpcompose.providers")
class ProvidersModule

@Module
@ComponentScan("org.macpry.kmpcompose.screens")
class ViewModelsModule
