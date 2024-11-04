package org.macpry.kmpcompose.repositories

import com.macpry.database.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.macpry.kmpcompose.data.local.INotesLocalData
import org.macpry.kmpcompose.logger.IKMPLogger
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
        val exception = Exception("Errr")
        val repository = NotesRepository(
            FakeNotesLocalData({ throw exception }, MutableStateFlow(emptyList())),
            FakeKMPLogger()
        )

        val result = repository.saveNote("aa")

        assertEquals(Result.failure(exception), result)
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

    class FakeKMPLogger : IKMPLogger {

        override fun logError(exception: Throwable) {
            TODO("Not yet implemented")
        }
    }
}
