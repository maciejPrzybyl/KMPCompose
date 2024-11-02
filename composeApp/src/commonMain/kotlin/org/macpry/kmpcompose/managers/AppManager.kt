package org.macpry.kmpcompose.managers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.datetime.LocalDateTime
import org.macpry.kmpcompose.data.ITimeProvider
import org.macpry.kmpcompose.data.network.INetworkData
import org.macpry.kmpcompose.data.network.ImageResponse

interface IAppManager {
    val timeFlow: Flow<LocalDateTime>
    suspend fun fetchImages(): Result<List<ImageResponse>>
}

class AppManager(
    private val timeProvider: ITimeProvider,
    private val networkData: INetworkData
) : IAppManager {

    override val timeFlow = timeProvider.currentDateTime.catch {
        println(it)
    }

    override suspend fun fetchImages() = runCatching {
        networkData.getImages()
    }

}
