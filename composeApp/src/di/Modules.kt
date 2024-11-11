import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

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
}
