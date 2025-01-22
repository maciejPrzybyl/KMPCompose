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
import org.macpry.kmpcompose.repositories.CurrentUser
import org.macpry.kmpcompose.repositories.IUserRepository
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
        val timeFlow = flow {
            emit(fakeTime1)
            delay(2)
            emit(fakeTime2)
        }
        val viewModel =
            createViewModel(timeFlow, Result.success(listOf(fakeImage)), 1, flowOf(0), flowOf(null))

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
        val timeFlow = flow {
            emit(fakeTime1)
            delay(2)
            emit(fakeTime2)
        }
        val exception = Exception("Exxx")
        val viewModel =
            createViewModel(timeFlow, Result.failure(exception), 3, flowOf(0), flowOf(null))

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
        val timeFlow = flow {
            emit(fakeTime1)
            delay(2)
            emit(fakeTime2)
        }
        val viewModel =
            createViewModel(timeFlow, Result.success(emptyList()), 1, flowOf(3, 2, 1), flowOf(null))

        viewModel.state.test {
            assertEquals(MainState(null, ImagesState.Init, 0), awaitItem())
            assertEquals(MainState(fakeTime1.toString(), ImagesState.Loading, 3), awaitItem())
            assertEquals(MainState(fakeTime1.toString(), ImagesState.Loading, 2), awaitItem())
            assertEquals(MainState(fakeTime1.toString(), ImagesState.Loading, 1), awaitItem())
        }
    }

    @Test
    fun emitCurrentUser() = runTest {
        val user1 = CurrentUser("1", "1em")
        val user2 = CurrentUser("2", "2em")
        val viewModel =
            createViewModel(
                flowOf(fakeTime1),
                Result.success(emptyList()),
                1,
                flowOf(0),
                flowOf(user1, user2)
            )

        viewModel.state.test {
            assertEquals(MainState(null, ImagesState.Init, 0, null), awaitItem())
            assertEquals(
                MainState(fakeTime1.toString(), ImagesState.Loading, 0, user1),
                awaitItem()
            )
            assertEquals(
                MainState(fakeTime1.toString(), ImagesState.Loading, 0, user2),
                awaitItem()
            )
        }
    }

    private fun createViewModel(
        fakeTimeFlow: Flow<LocalDateTime>,
        fetchImagesResult: Result<List<ImageResponse>>,
        imagesDelay: Long,
        progressFlow: Flow<Int>,
        currentUserFlow: Flow<CurrentUser?>
    ) = MainViewModel(
        FakeAppManager(fakeTimeFlow, fetchImagesResult, imagesDelay),
        FakeBackgroundWorker(progressFlow),
        FakeUserRepository(currentUserFlow)
    )

    class FakeAppManager(
        fakeTimeFlow: Flow<LocalDateTime>,
        private val fetchImagesResult: Result<List<ImageResponse>>,
        private val imagesDelay: Long
    ) : IAppManager {
        override val timeFlow: Flow<LocalDateTime> = fakeTimeFlow

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

    class FakeUserRepository(fakeUserFlow: Flow<CurrentUser?>) : IUserRepository {
        override val currentUserFlow: Flow<CurrentUser?> = fakeUserFlow
    }
}
