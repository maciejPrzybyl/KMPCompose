package org.macpry.kmpcompose.managers

import kotlinx.coroutines.flow.catch
import org.macpry.kmpcompose.data.ITimeProvider
import org.macpry.kmpcompose.data.network.INetworkData

class AppManager(
    private val timeProvider: ITimeProvider,
    private val networkData: INetworkData
) {

    internal fun timeFlow() = timeProvider.currentDateTime().catch {
        println(it)
    }

    internal suspend fun fetchImages() = runCatching {
        networkData.getImages()
    }

}
