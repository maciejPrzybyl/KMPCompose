package org.macpry.kmpcompose.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.koinApplication
import org.koin.ksp.generated.module

fun koinConfiguration() = koinApplication {
    modules(AppModule().module)
}

@Module//(includes = [ManagerModule::class, ViewModelsModule::class])
@ComponentScan
class AppModule

/*@Module
@ComponentScan("org.macpry.kmpcompose.manager")
class ManagerModule

@Module
@ComponentScan("org.macpry.kmpcompose.appviewmodel")
class ViewModelsModule*/
