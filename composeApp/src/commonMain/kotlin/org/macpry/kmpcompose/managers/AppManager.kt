package org.macpry.kmpcompose.managers

import org.koin.core.annotation.Single
import org.macpry.kmpcompose.network.Networking
import org.macpry.kmpcompose.providers.TimeProvider

@Single
class AppManager(
    private val timeProvider: TimeProvider,
    private val networking: Networking
) {

    fun timeFlow() = timeProvider.currentDateTime()

    suspend fun fetchImages() = networking.getImages()

}
