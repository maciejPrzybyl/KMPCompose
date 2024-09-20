package org.macpry.kmpcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class AppViewModel (
    //private val timeManager: TimeManager
) : ViewModel() {

    init {
        println("AppViewModel init")
    }

    override fun onCleared() {
        println("AppViewModel onCleared")
        super.onCleared()
    }

    val currentTime = //timeManager.timeFlow()
        flowOf(1)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

}
