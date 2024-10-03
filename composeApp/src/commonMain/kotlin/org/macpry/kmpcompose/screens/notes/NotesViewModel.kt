package org.macpry.kmpcompose.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.macpry.kmpcompose.managers.AppManager

@KoinViewModel
class NotesViewModel(
    private val appManager: AppManager
) : ViewModel() {

    private val _state = MutableStateFlow(NotesState(emptyList()))
    internal val state = _state.asStateFlow()

    internal fun saveNote(note: String) = viewModelScope.launch {
        appManager.saveNote(note)
            .onFailure {
                println(it)
            }
    }
}

data class NotesState(
    val texts: List<String>
)
