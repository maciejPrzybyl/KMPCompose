package com.macpry.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun dataStorePath(): Module

actual val datastoreModule: Module = module {
    includes(dataStorePath())
    single<DataStore<Preferences>> { createDataStore(get()) }
    single<KMPDatastore> { NoWasmDataStore(get()) }
}

fun createDataStore(path: String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        //TODO Include corruptionHandler
        produceFile = { path.toPath() }
    )

class NoWasmDataStore(
    private val dataStore: DataStore<Preferences>
) : KMPDatastore {
    override val selectedNumber: Flow<Int> = dataStore.data
        .catch {
            //TODO Log error
        }.map {
            it[NoWasmDataStoreKeys.SELECTED_NUMBER] ?: 0
        }

    override suspend fun setNumber(value: Int) {
        dataStore.edit {
            it[NoWasmDataStoreKeys.SELECTED_NUMBER] = value
        }
    }
}

private object NoWasmDataStoreKeys {
    val SELECTED_NUMBER = intPreferencesKey(KMPDatastoreKeys.SELECTED_NUMBER)
}

internal const val dataStoreFileName = "settings.preferences_pb"
