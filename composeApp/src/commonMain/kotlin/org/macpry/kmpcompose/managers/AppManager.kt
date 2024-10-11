package org.macpry.kmpcompose.managers

import kotlinx.coroutines.flow.catch
import org.macpry.kmpcompose.data.network.NetworkData
import org.macpry.kmpcompose.data.TimeProvider

class AppManager(
    private val timeProvider: TimeProvider,
    private val networkData: NetworkData
) {

    internal fun timeFlow() = timeProvider.currentDateTime().catch {
        println(it)
    }

    internal suspend fun fetchImages() = runCatching {
        networkData.getImages()
    }

}
