package org.macpry.kmpcompose.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.macpry.kmpcompose.data.network.ImageResponse
import org.macpry.kmpcompose.managers.AppManager

class MainViewModel(
    private val appManager: AppManager
) : ViewModel() {

    init {
        fetchImages()
    }

    internal val state = appManager.timeFlow().map {
        MainState(it.toString())
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MainState(null)
    )

    internal var images by mutableStateOf(emptyList<ImageResponse>())
        private set

    private fun fetchImages() = viewModelScope.launch {
        appManager.fetchImages()
            .onSuccess {
                images = it
            }.onFailure {
                println(it)
            }
    }
}

data class MainState(
    val currentTime: String?
)
