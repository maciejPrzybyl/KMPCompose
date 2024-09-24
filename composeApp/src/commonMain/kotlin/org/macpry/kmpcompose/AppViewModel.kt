package org.macpry.kmpcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class AppViewModel (
    appManager: AppManager
) : ViewModel() {

    val currentTime = appManager.timeFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

}
