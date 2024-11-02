package org.macpry.kmpcompose.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import org.macpry.kmpcompose.data.local.SettingsLocalData

interface ISettingsRepository {
    val settingsFlow: Flow<List<Pair<Int, Boolean>>>
    suspend fun saveSetting(value: Int): Result<Unit>
}

class SettingsRepository(
    private val settingsLocalData: SettingsLocalData
) : ISettingsRepository {

    override suspend fun saveSetting(value: Int) = runCatching {
        settingsLocalData.saveSetting(value)
    }

    override val settingsFlow = settingsLocalData.settingsFlow.catch {
        println(it)
    }

}