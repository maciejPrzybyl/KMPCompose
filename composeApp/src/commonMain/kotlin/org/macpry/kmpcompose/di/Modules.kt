package org.macpry.kmpcompose.di

import com.macpry.database.databaseModule
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.macpry.kmpcompose.data.TimeProvider
import org.macpry.kmpcompose.data.local.NotesLocalData
import org.macpry.kmpcompose.data.network.NetworkData
import org.macpry.kmpcompose.managers.AppManager
import org.macpry.kmpcompose.providers.KMPDispatchers
import org.macpry.kmpcompose.providers.provideDefaultDispatcher
import org.macpry.kmpcompose.providers.provideHttpClient
import org.macpry.kmpcompose.providers.provideIODispatcher
import org.macpry.kmpcompose.repositories.NotesRepository
import org.macpry.kmpcompose.screens.MainViewModel
import org.macpry.kmpcompose.screens.notes.NotesViewModel

fun appModule() = module {
    includes(
        dataModule,
        providersModule,
        managersModule,
        repositoriesModule,
        databaseModule,
        viewModelsModule
    )
}

val dataModule = module {
    factory { NotesLocalData(get(named(KMPDispatchers.IO)), get()) }
    factory { NetworkData(get(), get(named(KMPDispatchers.IO))) }
}

val providersModule = module {
    singleOf(::provideHttpClient)
    single<CoroutineDispatcher>(named(KMPDispatchers.DEFAULT)) { provideDefaultDispatcher() }
    single<CoroutineDispatcher>(named(KMPDispatchers.IO)) { provideIODispatcher() }
    single { TimeProvider(get(named(KMPDispatchers.IO))) }
}

val managersModule = module {
    factoryOf(::AppManager)
}

val repositoriesModule = module {
    factoryOf(::NotesRepository)
}

val viewModelsModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { NotesViewModel(get()) }
}
