package org.macpry.kmpcompose.data.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class SettingsLocalData(
    private val ioDispatcher: CoroutineDispatcher,

    ) {

    internal suspend fun saveSetting(value: Int): Unit = withContext(ioDispatcher) {

    }

    internal fun settingsFlow() = flowOf(
        (1..20).map {
            it to (it == 18)
        }
    ).flowOn(ioDispatcher)
}
