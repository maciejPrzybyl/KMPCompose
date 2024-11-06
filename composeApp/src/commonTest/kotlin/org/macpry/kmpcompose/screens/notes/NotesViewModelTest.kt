package org.macpry.kmpcompose.screens.notes

import app.cash.turbine.test
import com.macpry.database.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.macpry.kmpcompose.repositories.INotesRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun observeNotesState() = runTest {
        val notesFlow = MutableStateFlow<List<Note>>(emptyList())
        val viewModel = NotesViewModel(FakeNotesRepository(null, notesFlow))

        viewModel.notesState.test {
            assertEquals(NotesState(emptyList()), awaitItem())

            val note = Note("aaaa")
            notesFlow.emit(listOf(note))
            assertEquals(NotesState(listOf(note.content)), awaitItem())
        }
    }

    @Test
    fun validateNoteInput() = runTest {
        val viewModel = NotesViewModel(FakeNotesRepository(null, flowOf(emptyList())))

        assertEquals("" to false, viewModel.inputState.value)

        viewModel.updateInput(" ")
        assertEquals("" to false, viewModel.inputState.value)

        viewModel.updateInput("v")
        assertEquals("v" to true, viewModel.inputState.value)
    }

    @Test
    fun saveNoteError() = runTest {
        val exception = Exception("Save error")
        val viewModel =
            NotesViewModel(FakeNotesRepository(exception, flowOf(emptyList())))

        viewModel.error.test {
            viewModel.updateInput("Will fail")
            viewModel.saveNote()
            assertEquals(exception, awaitItem())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveNoteSuccess() = runTest {
        val viewModel =
            NotesViewModel(FakeNotesRepository(null, flowOf(emptyList())))

        viewModel.error.test {
            viewModel.updateInput("Will succeed")
            viewModel.saveNote()
            advanceUntilIdle()
            assertEquals("" to false, viewModel.inputState.value)
            expectNoEvents()
        }
    }

    class FakeNotesRepository(
        private val saveResultException: Exception?,
        fakeNotesFlow: Flow<List<Note>>
    ) : INotesRepository {
        override suspend fun saveNote(note: String) =
            saveResultException?.let { Result.failure(it) } ?: Result.success(Unit)

        override val notesFlow: Flow<List<Note>> = fakeNotesFlow
    }
}