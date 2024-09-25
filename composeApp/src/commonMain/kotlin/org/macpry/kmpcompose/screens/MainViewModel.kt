package org.macpry.kmpcompose.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel
import org.macpry.kmpcompose.managers.AppManager

@KoinViewModel
class MainViewModel(
    private val appManager: AppManager
) : ViewModel() {

    val state = appManager.timeFlow().map {
        MainState(it.toString())
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MainState(null)
    )

}

data class MainState(
    val currentTime: String?
)
