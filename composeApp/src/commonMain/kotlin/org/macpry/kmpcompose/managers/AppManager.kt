package org.macpry.kmpcompose.managers

import kotlinx.coroutines.flow.catch
import org.koin.core.annotation.Single
import org.macpry.kmpcompose.data.network.NetworkData
import org.macpry.kmpcompose.data.TimeProvider

@Single
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
