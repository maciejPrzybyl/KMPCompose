package org.macpry.kmpcompose.repositories

import kotlinx.coroutines.flow.catch
import org.macpry.kmpcompose.data.local.SettingsLocalData

class SettingsRepository(
    private val settingsLocalData: SettingsLocalData
) {

    internal suspend fun saveSetting(value: Int) = runCatching {
        settingsLocalData.saveSetting(value)
    }

    internal val settingsFlow = settingsLocalData.settingsFlow.catch {
        println(it)
    }

}