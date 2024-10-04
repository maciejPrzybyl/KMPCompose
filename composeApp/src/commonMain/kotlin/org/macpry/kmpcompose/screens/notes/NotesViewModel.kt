package org.macpry.kmpcompose.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.macpry.kmpcompose.managers.AppManager

@KoinViewModel
class NotesViewModel(
    private val appManager: AppManager
) : ViewModel() {

    val notesState = appManager.notesFlow().map {
        NotesState(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        NotesState(emptyList())
    )

    internal fun saveNote(note: String) = viewModelScope.launch {
        appManager.saveNote(note)
            .onFailure {
                println(it)
            }
    }
}

data class NotesState(
    val notes: List<String>
)
