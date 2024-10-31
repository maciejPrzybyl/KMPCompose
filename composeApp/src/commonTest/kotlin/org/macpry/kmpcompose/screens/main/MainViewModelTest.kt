package org.macpry.kmpcompose.screens.main

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
        val viewModel = createViewModel(Result.success(listOf(fakeImage)), 1)

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
        val viewModel = createViewModel(Result.failure(exception), 3)

        viewModel.state.test {
            assertEquals(MainState(null, ImagesState.Init), awaitItem())
            assertEquals(MainState(fakeTime1.toString(), ImagesState.Loading), awaitItem())
            assertEquals(MainState(fakeTime2.toString(), ImagesState.Loading), awaitItem())
            assertEquals(
                MainState(fakeTime2.toString(), ImagesState.Error(exception)), awaitItem()
            )
        }
    }

    private fun createViewModel(fetchImagesResult: Result<List<ImageResponse>>, imagesDelay: Long) =
        MainViewModel(FakeAppManager(fetchImagesResult, imagesDelay))

    class FakeAppManager(
        private val fetchImagesResult: Result<List<ImageResponse>>,
        private val imagesDelay: Long
    ) : IAppManager {
        override fun timeFlow(): Flow<LocalDateTime> = flow {
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
}
