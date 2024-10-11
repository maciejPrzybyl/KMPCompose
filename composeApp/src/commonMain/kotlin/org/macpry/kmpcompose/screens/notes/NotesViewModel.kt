package org.macpry.kmpcompose.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.macpry.kmpcompose.repositories.NotesRepository

class NotesViewModel(
    private val notesRepository: NotesRepository
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
                println(it)
            }
    }
}

data class NotesState(
    val notes: List<String>
)
