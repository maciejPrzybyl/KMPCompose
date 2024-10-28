package org.macpry.kmpcompose.screens

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.macpry.kmpcompose.data.ITimeProvider
import org.macpry.kmpcompose.data.network.INetworkData
import org.macpry.kmpcompose.data.network.ImageResponse
import org.macpry.kmpcompose.managers.AppManager
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest : KoinTest {

    private lateinit var viewModel: MainViewModel

    private val testModule = module {
        singleOf<ITimeProvider>(::FakeTimeProvider)
        factoryOf<INetworkData>(::FakeNetworkData)
        factoryOf(::AppManager)
        viewModelOf(::MainViewModel)
    }

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(testModule)
        }
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = get()
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun fetchImagesOnInit() = runTest {
        assertEquals(listOf(FakeNetworkData.fakeImage), viewModel.images)
    }

    @Test
    fun getCurrentTime() = runTest {
        viewModel.state.test {
            assertEquals(MainState(null), awaitItem())
            assertEquals(MainState(FakeTimeProvider.fakeTime.toString()), awaitItem())
        }
    }

    class FakeTimeProvider : ITimeProvider {

        override fun currentDateTime(): Flow<LocalDateTime> {
            return flowOf(fakeTime)
        }

        companion object {
            val fakeTime = LocalDateTime(2024, 12, 31, 23, 59)
        }
    }

    class FakeNetworkData : INetworkData {

        override suspend fun getImages(): List<ImageResponse> {
            return listOf(fakeImage)
        }

        companion object {
            val fakeImage = ImageResponse(31, "aaa", "auuu")
        }
    }

}
