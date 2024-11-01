package org.macpry.kmpcompose.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.macpry.kmpcompose.repositories.INotesRepository

class NotesViewModel(
    private val notesRepository: INotesRepository
) : ViewModel() {

    private val saveNoteState = MutableStateFlow<SaveState?>(null)

    val notesState = notesRepository.notesFlow().combine(saveNoteState) { notes, save ->
        NotesState(notes.map { it.content }, save)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        NotesState(emptyList(), null)
    )

    internal fun saveNote(note: String) = viewModelScope.launch {
        notesRepository.saveNote(note)
            .onSuccess {
                saveNoteState.value = SaveState.Success
            }
            .onFailure {
                saveNoteState.value = SaveState.Error(it)
            }
    }
}

data class NotesState(
    val notes: List<String>,
    val saveState: SaveState?
)

sealed class SaveState {
    data object Success : SaveState()
    data class Error(val error: Throwable) : SaveState()
}
