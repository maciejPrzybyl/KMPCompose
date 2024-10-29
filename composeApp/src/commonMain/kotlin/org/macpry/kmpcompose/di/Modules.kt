package org.macpry.kmpcompose.di

import com.macpry.database.databaseModule
import com.macpry.datastore.datastoreModule
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.macpry.kmpcompose.data.ITimeProvider
import org.macpry.kmpcompose.data.TimeProvider
import org.macpry.kmpcompose.data.local.NotesLocalData
import org.macpry.kmpcompose.data.local.SettingsLocalData
import org.macpry.kmpcompose.data.network.INetworkData
import org.macpry.kmpcompose.data.network.NetworkData
import org.macpry.kmpcompose.managers.AppManager
import org.macpry.kmpcompose.providers.KMPDispatchers
import org.macpry.kmpcompose.providers.provideDefaultDispatcher
import org.macpry.kmpcompose.providers.provideHttpClient
import org.macpry.kmpcompose.providers.provideIODispatcher
import org.macpry.kmpcompose.repositories.NotesRepository
import org.macpry.kmpcompose.repositories.SettingsRepository
import org.macpry.kmpcompose.screens.main.MainViewModel
import org.macpry.kmpcompose.screens.notes.NotesViewModel
import org.macpry.kmpcompose.screens.settings.SettingsViewModel

fun appModule() = module {
    includes(
        dataModule,
        providersModule,
        managersModule,
        repositoriesModule,
        databaseModule,
        datastoreModule,
        viewModelsModule
    )
}

val dataModule = module {
    factory { NotesLocalData(get(named(KMPDispatchers.IO)), get()) }
    factory<INetworkData> { NetworkData(get(), get(named(KMPDispatchers.IO))) }
    factory { SettingsLocalData(get(named(KMPDispatchers.IO)), get()) }
}

val providersModule = module {
    singleOf(::provideHttpClient)
    single<CoroutineDispatcher>(named(KMPDispatchers.DEFAULT)) { provideDefaultDispatcher() }
    single<CoroutineDispatcher>(named(KMPDispatchers.IO)) { provideIODispatcher() }
    single<ITimeProvider> { TimeProvider(get(named(KMPDispatchers.IO))) }
}

val managersModule = module {
    factoryOf(::AppManager)
}

val repositoriesModule = module {
    factoryOf(::NotesRepository)
    factoryOf(::SettingsRepository)
}

val viewModelsModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::NotesViewModel)
    viewModelOf(::SettingsViewModel)
}
