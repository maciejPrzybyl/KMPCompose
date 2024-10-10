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
class NotesLocalData(
    @Named(KMPDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    //private val kmpDatabase: KMPDatabase
) {

    internal suspend fun saveNote(note: String): Unit = withContext(ioDispatcher) {
        //kmpDatabase.noteDao().insert(DbNote(content = note))
    }

    /*fun notesFlow() = kmpDatabase.noteDao().getAll()
        .flowOn(ioDispatcher)*/

    fun notesFlow() = flow {
        emit(listOf("bbb", "aaa"))
        delay(1.seconds)
    }.flowOn(ioDispatcher)
}