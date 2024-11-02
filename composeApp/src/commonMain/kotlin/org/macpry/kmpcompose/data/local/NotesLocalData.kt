package org.macpry.kmpcompose.data.local

import com.macpry.database.KMPDatabase
import com.macpry.database.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class NotesLocalData(
    private val ioDispatcher: CoroutineDispatcher,
    private val kmpDatabase: KMPDatabase
) {

    internal suspend fun saveNote(note: String): Unit = withContext(ioDispatcher) {
        kmpDatabase.noteDao().insert(Note(content = note))
    }

    val notesFlow = kmpDatabase.noteDao().getAll()
        .flowOn(ioDispatcher)
}
