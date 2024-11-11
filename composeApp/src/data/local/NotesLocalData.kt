import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

interface INotesLocalData {
    val notesFlow: Flow<List<Note>>
    suspend fun saveNote(note: String)
}

class NotesLocalData(
    private val ioDispatcher: CoroutineDispatcher,
    private val kmpDatabase: KMPDatabase
) : INotesLocalData {

    override suspend fun saveNote(note: String) = withContext(ioDispatcher) {
        kmpDatabase.noteDao().insert(Note(content = note))
    }

    override val notesFlow = kmpDatabase.noteDao().getAll()
        .flowOn(ioDispatcher)
}
