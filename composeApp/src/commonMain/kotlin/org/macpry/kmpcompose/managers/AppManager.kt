package org.macpry.kmpcompose.managers

import kotlinx.coroutines.flow.catch
import org.koin.core.annotation.Single
import org.macpry.kmpcompose.data.local.LocalData
import org.macpry.kmpcompose.data.network.NetworkData
import org.macpry.kmpcompose.data.TimeProvider

@Single
class AppManager(
    private val timeProvider: TimeProvider,
    private val networkData: NetworkData,
    private val localData: LocalData
) {

    internal fun timeFlow() = timeProvider.currentDateTime().catch {
        println(it)
    }

    internal suspend fun fetchImages() = runCatching {
        networkData.getImages()
    }

    internal suspend fun saveNote(note: String) = runCatching {
        localData.saveNote(note)
    }

    internal fun notesFlow() = localData.notesFlow().catch {
        println(it)
    }

}
