package org.macpry.kmpcompose.data.local

import com.macpry.datastore.KMPDatastore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface ISettingsLocalData {
    val settingsFlow: Flow<List<Pair<Int, Boolean>>>
    suspend fun saveSetting(value: Int)
}

class SettingsLocalData(
    private val ioDispatcher: CoroutineDispatcher,
    private val kmpDatastore: KMPDatastore
) : ISettingsLocalData {

    override suspend fun saveSetting(value: Int): Unit = withContext(ioDispatcher) {
        kmpDatastore.setNumber(value)
    }

    override val settingsFlow = kmpDatastore.selectedNumber.map { selectedValue ->
        (1..20).map {
            it to (it == selectedValue)
        }
    }.flowOn(ioDispatcher)
}
