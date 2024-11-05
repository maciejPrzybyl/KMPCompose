package org.macpry.kmpcompose.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import org.macpry.kmpcompose.data.local.ISettingsLocalData
import org.macpry.kmpcompose.logger.IKMPLogger

interface ISettingsRepository {
    val settingsFlow: Flow<List<Pair<Int, Boolean>>>
    suspend fun saveSetting(value: Int): Result<Unit>
}

class SettingsRepository(
    private val settingsLocalData: ISettingsLocalData,
    private val kmpLogger: IKMPLogger
) : ISettingsRepository {

    override suspend fun saveSetting(value: Int) = runCatching {
        settingsLocalData.saveSetting(value)
    }.onFailure {
        kmpLogger.logError(it)
    }

    override val settingsFlow = settingsLocalData.settingsFlow.catch {
        println(it)
    }

}