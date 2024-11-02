package org.macpry.kmpcompose.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.macpry.kmpcompose.repositories.ISettingsRepository

class SettingsViewModel(
    private val settingsRepository: ISettingsRepository
) : ViewModel() {

    val settingsState = settingsRepository.settingsFlow.map {
        SettingsState(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SettingsState(emptyList())
    )

    internal fun saveSetting(value: Int) = viewModelScope.launch {
        settingsRepository.saveSetting(value)
            .onFailure {
                println(it)
            }
    }
}

data class SettingsState(
    val settings: List<Pair<Int, Boolean>>
)
