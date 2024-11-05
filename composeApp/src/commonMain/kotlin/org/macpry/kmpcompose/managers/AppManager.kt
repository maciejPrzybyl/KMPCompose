package org.macpry.kmpcompose.managers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.datetime.LocalDateTime
import org.macpry.kmpcompose.data.ITimeProvider
import org.macpry.kmpcompose.data.network.INetworkData
import org.macpry.kmpcompose.data.network.ImageResponse
import org.macpry.kmpcompose.logger.IKMPLogger

interface IAppManager {
    val timeFlow: Flow<LocalDateTime>
    suspend fun fetchImages(): Result<List<ImageResponse>>
}

class AppManager(
    private val timeProvider: ITimeProvider,
    private val networkData: INetworkData,
    private val kmpLogger: IKMPLogger
) : IAppManager {

    override val timeFlow = timeProvider.currentDateTime.catch {
        kmpLogger.logError(it)
    }

    override suspend fun fetchImages() = runCatching {
        networkData.getImages()
    }.onFailure {
        kmpLogger.logError(it)
    }

}
