package org.macpry.kmpcompose.managers

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import org.macpry.kmpcompose.data.ITimeProvider
import org.macpry.kmpcompose.data.network.INetworkData
import org.macpry.kmpcompose.data.network.ImageResponse
import org.macpry.kmpcompose.logger.FakeKMPLogger
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AppManagerTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun timeFlowError() = runTest {
        var handledException: Throwable? = null
        val exception = Exception("Time err")
        val dateTime1 = LocalDateTime(1, 2, 4, 5, 6)
        val fakeTimeFlow = flow {
            emit(dateTime1)
            throw exception
        }
        val manager = AppManager(
            FakeTimeProvider(fakeTimeFlow),
            FakeNetworkData(emptyList()),
            FakeKMPLogger { handledException = it }
        )

        manager.timeFlow.test {
            assertEquals(dateTime1, awaitItem())
            assertEquals(exception, handledException)
            awaitComplete()
        }
    }

    class FakeTimeProvider(fakeTimeFlow: Flow<LocalDateTime>) : ITimeProvider {
        override val currentDateTime: Flow<LocalDateTime> = fakeTimeFlow
    }

    class FakeNetworkData(private val fakeImages: List<ImageResponse>) : INetworkData {
        override suspend fun getImages(): List<ImageResponse> = fakeImages
    }
}