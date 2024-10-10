package org.macpry.kmpcompose.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.macpry.kmpcompose.screens.MainViewModel
import org.macpry.kmpcompose.screens.notes.NotesViewModel

fun appModule() = module {
    includes(
        AppModule().module,
        viewModelsModule,
        //databaseModule()
    )
}

//TODO Use below single module when @KoinViewModel will be stable
@Module(includes = [DataModule::class, ProvidersModule::class, ManagersModule::class, RepositoriesModule::class])
internal class AppModule

@Module
@ComponentScan("org.macpry.kmpcompose.data")
internal class DataModule

@Module
@ComponentScan("org.macpry.kmpcompose.providers")
internal class ProvidersModule

@Module
@ComponentScan("org.macpry.kmpcompose.managers")
internal class ManagersModule

@Module
@ComponentScan("org.macpry.kmpcompose.repositories")
internal class RepositoriesModule

private val viewModelsModule = module {
    viewModel { MainViewModel(get(), /*get()*/) }
    viewModel { NotesViewModel(get()) }
}
