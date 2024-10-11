package org.macpry.kmpcompose.repositories

import kotlinx.coroutines.flow.catch
import org.macpry.kmpcompose.data.local.NotesLocalData

class NotesRepository(
    private val notesLocalData: NotesLocalData
) {

    internal suspend fun saveNote(note: String) = runCatching {
        notesLocalData.saveNote(note)
    }

    internal fun notesFlow() = notesLocalData.notesFlow().catch {
        println(it)
    }

}