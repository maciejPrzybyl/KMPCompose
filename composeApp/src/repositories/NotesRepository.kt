import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

interface INotesRepository {
    suspend fun saveNote(note: String): Result<Unit>
    val notesFlow: Flow<List<Note>>
}

class NotesRepository(
    private val notesLocalData: INotesLocalData,
    private val logger: IKMPLogger
) : INotesRepository {

    override suspend fun saveNote(note: String) = runCatching {
        notesLocalData.saveNote(note)
    }.onFailure {
        logger.logError(it)
    }

    override val notesFlow = notesLocalData.notesFlow.catch {
        logger.logError(it)
    }

}