package org.macpry.kmpcompose.screens.dblist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DbListViewModel: ViewModel() {

    private val _state = MutableStateFlow(DbListState(emptyList()))
    val state = _state.asStateFlow()

    internal fun saveText(text: String) {

    }
}

data class DbListState(
    val texts: List<String>
)
