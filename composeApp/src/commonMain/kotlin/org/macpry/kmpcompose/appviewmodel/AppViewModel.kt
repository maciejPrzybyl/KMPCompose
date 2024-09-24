package org.macpry.kmpcompose.appviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel
import org.macpry.kmpcompose.manager.AppManager

@KoinViewModel
class AppViewModel (
    private val appManager: AppManager
) : ViewModel() {

    val currentTime = appManager.timeFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

}
