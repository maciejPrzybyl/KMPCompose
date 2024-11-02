package org.macpry.kmpcompose.repositories

import com.macpry.database.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import org.macpry.kmpcompose.data.local.NotesLocalData
import org.macpry.kmpcompose.logger.IKMPLogger

interface INotesRepository {
    suspend fun saveNote(note: String): Result<Unit>
    val notesFlow: Flow<List<Note>>
}

class NotesRepository(
    private val notesLocalData: NotesLocalData,
    private val logger: IKMPLogger
) : INotesRepository {

    override suspend fun saveNote(note: String) = runCatching {
        notesLocalData.saveNote(note)
    }

    override val notesFlow = notesLocalData.notesFlow.catch {
        logger.logError(it)
    }

}