package org.macpry.kmpcompose.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel
import org.macpry.kmpcompose.managers.AppManager

@KoinViewModel
class MainViewModel(
    private val appManager: AppManager
) : ViewModel() {

    private var inputText by mutableStateOf("")

    fun updateInput(value: String) {
        inputText = value
    }

    val state = appManager.timeFlow().combine(
        snapshotFlow { inputText }
    ) { currentTime, inputText ->
        MainState(currentTime.toString(), inputText)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MainState(null, null)
    )

}

data class MainState(
    val currentTime: String?,
    val inputText: String?
)
