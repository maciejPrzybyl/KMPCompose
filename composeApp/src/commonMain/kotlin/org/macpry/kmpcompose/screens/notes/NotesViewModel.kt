package org.macpry.kmpcompose.screens.notes

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.macpry.kmpcompose.repositories.INotesRepository

class NotesViewModel(
    private val notesRepository: INotesRepository
) : ViewModel() {

    var inputState = mutableStateOf("" to false)
        private set

    internal fun updateInput(text: String) {
        val isValid = text.isNotBlank()
        inputState.value = (if (isValid) text else "") to isValid
    }

    val notesState = notesRepository.notesFlow().map {
        NotesState(it.map { it.content })
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        NotesState(emptyList())
    )

    private val _error = MutableSharedFlow<Throwable>(extraBufferCapacity = 1)
    val error = _error.asSharedFlow()

    internal fun saveNote() = viewModelScope.launch {
        notesRepository.saveNote(inputState.value.first)
            .onSuccess {
                inputState.value = "" to false
            }.onFailure {
                _error.emit(it)
            }
    }
}

data class NotesState(
    val notes: List<String>
)
