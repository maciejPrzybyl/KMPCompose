package org.macpry.kmpcompose.data.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.macpry.kmpcompose.providers.KMPDispatchers
import kotlin.time.Duration.Companion.seconds

@Single
class LocalData(
    @Named(KMPDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {

    internal suspend fun saveNote(note: String): Unit = withContext(ioDispatcher) {

    }

    fun notesFlow() = flow {
        emit(listOf("bbb", "aaa"))
        delay(1.seconds)
    }.flowOn(ioDispatcher)
}