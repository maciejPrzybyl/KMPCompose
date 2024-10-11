package org.macpry.kmpcompose.data.local

import com.macpry.database.DbNote
import com.macpry.database.KMPDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class NotesLocalData(
    private val ioDispatcher: CoroutineDispatcher,
    private val kmpDatabase: KMPDatabase
) {

    internal suspend fun saveNote(note: String): Unit = withContext(ioDispatcher) {
        kmpDatabase.noteDao().insert(DbNote(content = note))
    }

    fun notesFlow() = kmpDatabase.noteDao().getAll()
        .flowOn(ioDispatcher)
}
