package org.macpry.kmpcompose.repositories

import app.cash.turbine.test
import com.macpry.database.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.macpry.kmpcompose.data.local.INotesLocalData
import org.macpry.kmpcompose.logger.IKMPLogger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class NotesRepositoryTest {

    /*@BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }*/

    @Test
    fun saveNoteError() = runTest {
        var exceptionConsumed: Boolean = false
        val exception = Exception("Errr")
        val repository = NotesRepository(
            FakeNotesLocalData({ throw exception }, MutableStateFlow(emptyList())),
            FakeKMPLogger { exceptionConsumed = true }
        )

        val result = repository.saveNote("aa")

        assertEquals(Result.failure(exception), result)
        assertTrue(exceptionConsumed)
    }

    @Test
    fun saveNoteSuccess() = runTest {
        val repository = NotesRepository(
            FakeNotesLocalData({ }, MutableStateFlow(emptyList())),
            FakeKMPLogger({})
        )

        val result = repository.saveNote("bb")

        assertEquals(Result.success(Unit), result)
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
            assertEquals(handledException, exception)
            awaitComplete()
        }
    }

    class FakeNotesLocalData(
        private val saveNoteAction: () -> Unit,
        fakeNotesFlow: Flow<List<Note>>
    ) : INotesLocalData {

        override val notesFlow: Flow<List<Note>> = fakeNotesFlow

        override suspend fun saveNote(note: String) {
            saveNoteAction()
        }
    }

    class FakeKMPLogger(private val exceptionConsumed: (Throwable) -> Unit) : IKMPLogger {

        override fun logError(exception: Throwable) {
            exceptionConsumed(exception)
        }
    }
}
