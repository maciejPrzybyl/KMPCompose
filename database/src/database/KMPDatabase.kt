import kotlinx.coroutines.flow.Flow
import org.koin.core.module.Module

expect val databaseModule: Module

interface KMPDatabase {
    fun noteDao(): NoteDao
}

interface NoteDao {
    suspend fun insert(note: Note)
    fun getAll(): Flow<List<Note>>
}

data class Note(
    val content: String
)
