package org.macpry.kmpcompose.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.macpry.kmpcompose.data.network.ImageResponse
import org.macpry.kmpcompose.managers.IAppManager

class MainViewModel(
    private val appManager: IAppManager
) : ViewModel() {

    private val imagesState = MutableStateFlow<ImagesState>(ImagesState.Init)

    init {
        fetchImages()
    }

    private fun fetchImages() = viewModelScope.launch {
        imagesState.value = ImagesState.Loading
        appManager.fetchImages()
            .onSuccess {
                imagesState.value = ImagesState.Success(it)
            }.onFailure {
                println(it)
                imagesState.value = ImagesState.Error(it)
            }
    }

    internal val state = appManager.timeFlow().combine(imagesState) { time, images ->
        MainState(time.toString(), images)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MainState(null, ImagesState.Init)
    )
}

data class MainState(
    val currentTime: String?,
    val imagesState: ImagesState
)

sealed class ImagesState {
    data object Init : ImagesState()
    data object Loading : ImagesState()
    data class Success(val images: List<ImageResponse>) : ImagesState()
    data class Error(val error: Throwable) : ImagesState()
}
