package org.macpry.kmpcompose.data.local

import com.macpry.datastore.KMPDatastore
import kotlinx.coroutines.flow.Flow

interface ISettingsLocalData {
    val settingsFlow: Flow<Int>
    suspend fun saveSetting(value: Int)
}

class SettingsLocalData(
    private val kmpDatastore: KMPDatastore
) : ISettingsLocalData {

    override suspend fun saveSetting(value: Int): Unit = kmpDatastore.setNumber(value)

    override val settingsFlow = kmpDatastore.selectedNumber
}
