package org.macpry.kmpcompose.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.koinApplication
import org.koin.ksp.generated.module

fun koinConfiguration() = koinApplication {
    modules(AppModule().module)
}

@Module(includes = [ManagersModule::class, ViewModelsModule::class])
class AppModule

@Module
@ComponentScan("org.macpry.kmpcompose.managers")
class ManagersModule

@Module
@ComponentScan("org.macpry.kmpcompose.screens")
class ViewModelsModule
