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
import org.macpry.kmpcompose.data.network.NetworkData

@KoinViewModel
class MainViewModel(
    private val appManager: AppManager
) : ViewModel() {

    init {
        fetchImages()
    }

    private var inputText by mutableStateOf("")

    internal fun updateInput(value: String) {
        inputText = value
    }

    internal val state = appManager.timeFlow().combine(
        snapshotFlow { inputText }
    ) { currentTime, inputText ->
        MainState(currentTime.toString(), inputText)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MainState(null, null)
    )

    internal var images by mutableStateOf(emptyList<NetworkData.ImageResponse>())
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
    val currentTime: String?,
    val inputText: String?
)
