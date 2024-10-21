package org.macpry.kmpcompose.data.local

import com.macpry.datastore.KMPDatastore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SettingsLocalData(
    private val ioDispatcher: CoroutineDispatcher,
    private val kmpDatastore: KMPDatastore
) {

    internal suspend fun saveSetting(value: Int): Unit = withContext(ioDispatcher) {
        kmpDatastore.setNumber(value)
    }

    internal fun settingsFlow() = kmpDatastore.getSelectedNumber().map { selectedValue ->
        (1..20).map {
            it to (it == selectedValue)
        }
    }.flowOn(ioDispatcher)
}
