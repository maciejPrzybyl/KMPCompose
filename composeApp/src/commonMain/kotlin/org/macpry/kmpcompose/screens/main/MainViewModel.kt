package org.macpry.kmpcompose.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.macpry.kmpcompose.data.network.ImageResponse
import org.macpry.kmpcompose.managers.IAppManager
import org.macpry.kmpcompose.services.worker.BackgroundWorker

class MainViewModel(
    private val appManager: IAppManager,
    private val backgroundWorker: BackgroundWorker
) : ViewModel() {

    private fun fetchImages() = flow {
        emit(ImagesState.Loading)
        appManager.fetchImages()
            .onSuccess {
                emit(ImagesState.Success(it))
            }.onFailure {
                emit(ImagesState.Error(it))
            }
    }

    internal val state = combine(
        appManager.timeFlow,
        fetchImages(),
        backgroundWorker.progressFlow
    ) { time, images, progress ->
        MainState(time.toString(), images, progress)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MainState(null, ImagesState.Init, 0)
    )

    internal fun startWorker() {
        backgroundWorker.start()
    }
}

data class MainState(
    val currentTime: String? = null,
    val imagesState: ImagesState,
    val workerProgress: Int = 0
)

sealed class ImagesState {
    data object Init : ImagesState()
    data object Loading : ImagesState()
    data class Success(val images: List<ImageResponse>) : ImagesState()
    data class Error(val error: Throwable) : ImagesState()
}
