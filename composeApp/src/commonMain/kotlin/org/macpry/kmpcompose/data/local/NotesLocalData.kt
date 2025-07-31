package org.macpry.kmpcompose.data.local

import com.macpry.database.KMPDatabase
import com.macpry.database.Note
import kotlinx.coroutines.flow.Flow


interface INotesLocalData {
    val notesFlow: Flow<List<Note>>
    suspend fun saveNote(note: String)
}

class NotesLocalData(
    private val kmpDatabase: KMPDatabase
) : INotesLocalData {

    override suspend fun saveNote(note: String) = kmpDatabase.noteDao().insert(Note(content = note))

    override val notesFlow = kmpDatabase.noteDao().getAll()
}
