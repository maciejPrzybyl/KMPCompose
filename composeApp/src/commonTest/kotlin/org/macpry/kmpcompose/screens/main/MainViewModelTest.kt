package org.macpry.kmpcompose.screens.main

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import org.macpry.kmpcompose.data.network.ImageResponse
import org.macpry.kmpcompose.managers.IAppManager
import org.macpry.kmpcompose.screens.main.MainViewModelTest.FakeAppManager.Companion.fakeImage
import org.macpry.kmpcompose.screens.main.MainViewModelTest.FakeAppManager.Companion.fakeTime1
import org.macpry.kmpcompose.screens.main.MainViewModelTest.FakeAppManager.Companion.fakeTime2
import org.macpry.kmpcompose.services.worker.BackgroundWorker
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchImagesSuccessfully() = runTest {
        val viewModel = createViewModel(Result.success(listOf(fakeImage)), 1, flowOf(0))

        viewModel.state.test {
            assertEquals(MainState(null, ImagesState.Init), awaitItem())
            assertEquals(MainState(fakeTime1.toString(), ImagesState.Loading), awaitItem())
            assertEquals(
                MainState(fakeTime1.toString(), ImagesState.Success(listOf(fakeImage))), awaitItem()
            )
            assertEquals(
                MainState(fakeTime2.toString(), ImagesState.Success(listOf(fakeImage))), awaitItem()
            )
        }
    }

    @Test
    fun fetchImagesError() = runTest {
        val exception = Exception("Exxx")
        val viewModel = createViewModel(Result.failure(exception), 3, flowOf(0))

        viewModel.state.test {
            assertEquals(MainState(null, ImagesState.Init), awaitItem())
            assertEquals(MainState(fakeTime1.toString(), ImagesState.Loading), awaitItem())
            assertEquals(MainState(fakeTime2.toString(), ImagesState.Loading), awaitItem())
            assertEquals(
                MainState(fakeTime2.toString(), ImagesState.Error(exception)), awaitItem()
            )
        }
    }

    @Test
    fun emitWorkerProgress() = runTest {
        val viewModel = createViewModel(Result.success(emptyList()), 1, flowOf(3, 2, 1))

        viewModel.state.test {
            assertEquals(MainState(null, ImagesState.Init, 0), awaitItem())
            assertEquals(MainState(fakeTime1.toString(), ImagesState.Loading, 3), awaitItem())
            assertEquals(MainState(fakeTime1.toString(), ImagesState.Loading, 2), awaitItem())
            assertEquals(MainState(fakeTime1.toString(), ImagesState.Loading, 1), awaitItem())
        }
    }

    private fun createViewModel(
        fetchImagesResult: Result<List<ImageResponse>>,
        imagesDelay: Long,
        progressFlow: Flow<Int>
    ) = MainViewModel(
        FakeAppManager(fetchImagesResult, imagesDelay),
        FakeBackgroundWorker(progressFlow)
    )

    class FakeAppManager(
        private val fetchImagesResult: Result<List<ImageResponse>>,
        private val imagesDelay: Long
    ) : IAppManager {
        override val timeFlow: Flow<LocalDateTime> = flow {
            emit(fakeTime1)
            delay(2)
            emit(fakeTime2)
        }

        override suspend fun fetchImages(): Result<List<ImageResponse>> {
            delay(imagesDelay)
            return fetchImagesResult
        }

        companion object {
            val fakeTime1 = LocalDateTime(2024, 12, 31, 23, 58)
            val fakeTime2 = LocalDateTime(2024, 12, 31, 23, 59)

            val fakeImage = ImageResponse(31, "aaa", "auuu")
        }
    }

    class FakeBackgroundWorker(fakeProgressFlow: Flow<Int>) : BackgroundWorker() {
        override fun start() {}

        override val progressFlow: Flow<Int> = fakeProgressFlow

        override val tag: String = "FAKE_TAG"
    }
}
