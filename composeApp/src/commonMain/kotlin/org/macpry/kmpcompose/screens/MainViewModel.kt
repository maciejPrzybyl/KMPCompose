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
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.macpry.kmpcompose.managers.AppManager
import org.macpry.kmpcompose.managers.network.Networking

@KoinViewModel
class MainViewModel(
    private val appManager: AppManager
) : ViewModel() {

    init {
        fetchImages()
    }

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

    var images by mutableStateOf(emptyList<Networking.ImageResponse>())
        private set

    private fun fetchImages() = viewModelScope.launch {
        appManager.fetchImages()
            .onSuccess {
                images = it.images
            }.onFailure {
                println(it)
            }
    }

}

data class MainState(
    val currentTime: String?,
    val inputText: String?
)
