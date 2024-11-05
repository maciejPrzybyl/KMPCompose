package org.macpry.kmpcompose.repositories

import app.cash.turbine.test
import com.macpry.database.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.macpry.kmpcompose.data.local.INotesLocalData
import org.macpry.kmpcompose.logger.FakeKMPLogger
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class NotesRepositoryTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun saveNoteError() = runTest {
        var handledException: Throwable? = null
        val exception = Exception("Errr")
        val repository = NotesRepository(
            FakeNotesLocalData({ throw exception }, MutableStateFlow(emptyList())),
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
            FakeNotesLocalData({ noteToSave = it }, MutableStateFlow(emptyList())),
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
