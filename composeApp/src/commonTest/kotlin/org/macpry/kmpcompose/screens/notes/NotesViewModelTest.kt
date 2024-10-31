package org.macpry.kmpcompose.screens.notes

import app.cash.turbine.test
import com.macpry.database.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
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

    class FakeNotesRepository(
        private val saveResultException: Exception?,
        private val notesFlow: MutableStateFlow<List<Note>>
    ) : INotesRepository {
        override suspend fun saveNote(note: String) =
            saveResultException?.let { Result.failure(it) } ?: Result.success(Unit)

        override fun notesFlow(): Flow<List<Note>> = notesFlow
    }
}