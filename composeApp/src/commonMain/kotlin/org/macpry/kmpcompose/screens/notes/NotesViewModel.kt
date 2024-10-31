package org.macpry.kmpcompose.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.macpry.kmpcompose.repositories.INotesRepository

class NotesViewModel(
    private val notesRepository: INotesRepository
) : ViewModel() {

    val notesState = notesRepository.notesFlow().map {
        NotesState(it.map { it.content })
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        NotesState(emptyList())
    )

    internal fun saveNote(note: String) = viewModelScope.launch {
        notesRepository.saveNote(note)
            .onFailure {
                //TODO Show error
            }
    }
}

data class NotesState(
    val notes: List<String>
)
