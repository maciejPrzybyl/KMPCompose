package org.macpry.kmpcompose.screens.details

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DetailsViewModel(
    //TODO Seems like Koin annotations with compose cannot handle SavedStateHandle properly
    //private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    /*fun setArgValue(value: String) = savedStateHandle.set("ARG_KEY", value)

    val state = savedStateHandle.getStateFlow("ARG_KEY", null).map {
        DetailsState(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DetailsState(null)
    )*/

    private val _state = MutableStateFlow(DetailsState(""))
    val state = _state.asStateFlow()

    fun setArg(arg: String?) {
        _state.update {
            it.copy(argValue = arg)
        }
    }
}

data class DetailsState(
    val argValue: String?
)
