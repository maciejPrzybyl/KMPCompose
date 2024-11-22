package org.macpry.kmpcompose.di

import com.macpry.database.databaseModule
import com.macpry.datastore.datastoreModule
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.macpry.kmpcompose.data.ITimeProvider
import org.macpry.kmpcompose.data.TimeProvider
import org.macpry.kmpcompose.data.local.INotesLocalData
import org.macpry.kmpcompose.data.local.ISettingsLocalData
import org.macpry.kmpcompose.data.local.NotesLocalData
import org.macpry.kmpcompose.data.local.SettingsLocalData
import org.macpry.kmpcompose.data.network.INetworkData
import org.macpry.kmpcompose.data.network.NetworkData
import org.macpry.kmpcompose.logger.IKMPLogger
import org.macpry.kmpcompose.logger.KMPLogger
import org.macpry.kmpcompose.managers.AppManager
import org.macpry.kmpcompose.managers.IAppManager
import org.macpry.kmpcompose.providers.KMPDispatchers
import org.macpry.kmpcompose.providers.provideDefaultDispatcher
import org.macpry.kmpcompose.providers.provideHttpClient
import org.macpry.kmpcompose.providers.provideIODispatcher
import org.macpry.kmpcompose.repositories.INotesRepository
import org.macpry.kmpcompose.repositories.ISettingsRepository
import org.macpry.kmpcompose.repositories.NotesRepository
import org.macpry.kmpcompose.repositories.SettingsRepository
import org.macpry.kmpcompose.screens.main.MainViewModel
import org.macpry.kmpcompose.screens.maps.MapsViewModel
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
    factory<INotesLocalData> { NotesLocalData(get(named(KMPDispatchers.IO)), get()) }
    factory<INetworkData> { NetworkData(get(), get(named(KMPDispatchers.IO))) }
    factory<ISettingsLocalData> { SettingsLocalData(get(named(KMPDispatchers.IO)), get()) }
}

val providersModule = module {
    singleOf(::provideHttpClient)
    single<CoroutineDispatcher>(named(KMPDispatchers.DEFAULT)) { provideDefaultDispatcher() }
    single<CoroutineDispatcher>(named(KMPDispatchers.IO)) { provideIODispatcher() }
    single<ITimeProvider> { TimeProvider(get(named(KMPDispatchers.IO))) }
    factoryOf(::KMPLogger) bind IKMPLogger::class
}

val managersModule = module {
    factoryOf(::AppManager) bind IAppManager::class
}

val repositoriesModule = module {
    factoryOf(::NotesRepository) bind INotesRepository::class
    factoryOf(::SettingsRepository) bind ISettingsRepository::class
}

val viewModelsModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::NotesViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::MapsViewModel)
}
