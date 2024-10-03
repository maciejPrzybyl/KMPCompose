package org.macpry.kmpcompose.data.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.macpry.kmpcompose.providers.KMPDispatchers

@Single
class LocalData(
    @Named(KMPDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {

    internal suspend fun saveNote(note: String): Unit = withContext(ioDispatcher) {

    }
}