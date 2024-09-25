package org.macpry.kmpcompose.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DetailsViewModel(
    //TODO Seems like Koin annotations with compose cannot handle SavedStateHandle properly
    //private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //fun setArgValue(value: String) = savedStateHandle.set("ARG_KEY", value)

    val state = /*savedStateHandle.getStateFlow("ARG_KEY", null).map {
        DetailsState(it)
    }*/flowOf(DetailsState("aa")).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DetailsState(null)
    )
}

data class DetailsState(
    val argValue: String?
)
