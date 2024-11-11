import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class NotesRepositoryTest {

    @Test
    fun saveNoteError() = runTest {
        var handledException: Throwable? = null
        val exception = Exception("Errr")
        val repository = NotesRepository(
            FakeNotesLocalData({ throw exception }, flowOf(emptyList())),
            FakeKMPLogger { handledException = it }
        )

        val result = repository.saveNote("aa")

        assertEquals(Result.failure(exception), result)
        assertEquals(exception, handledException)
    }

    @Test
    fun saveNoteSuccess() = runTest {
        var noteToSave: String? = null
        val repository = NotesRepository(
            FakeNotesLocalData({ noteToSave = it }, flowOf(emptyList())),
            FakeKMPLogger({})
        )

        val result = repository.saveNote("bb")

        assertEquals(Result.success(Unit), result)
        assertEquals("bb", noteToSave)
    }

    @Test
    fun logNotesFlowError() = runTest {
        var handledException: Throwable? = null
        val exception = Exception("FErrr")
        val repository = NotesRepository(
            FakeNotesLocalData({}, flow { throw exception }),
            FakeKMPLogger { handledException = it }
        )

        repository.notesFlow.test {
            assertEquals(exception, handledException)
            awaitComplete()
        }
    }

    class FakeNotesLocalData(
        private val saveNoteAction: (String) -> Unit,
        fakeNotesFlow: Flow<List<Note>>
    ) : INotesLocalData {

        override val notesFlow: Flow<List<Note>> = fakeNotesFlow

        override suspend fun saveNote(note: String) {
            saveNoteAction(note)
        }
    }
}
