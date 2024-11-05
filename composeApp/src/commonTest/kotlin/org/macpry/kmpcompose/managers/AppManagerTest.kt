package org.macpry.kmpcompose.managers

import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.macpry.kmpcompose.data.ITimeProvider
import org.macpry.kmpcompose.data.network.INetworkData
import org.macpry.kmpcompose.data.network.ImageResponse
import org.macpry.kmpcompose.logger.FakeKMPLogger
import kotlin.test.Test
import kotlin.test.assertEquals

class AppManagerTest {

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
            FakeNetworkData { emptyList() },
            FakeKMPLogger { handledException = it }
        )

        manager.timeFlow.test {
            assertEquals(dateTime1, awaitItem())
            assertEquals(exception, handledException)
            awaitComplete()
        }
    }

    @Test
    fun fetchImagesSuccess() = runTest {
        val images = listOf(ImageResponse(3, "urr", "autt"))
        val manager = AppManager(
            FakeTimeProvider(flowOf()),
            FakeNetworkData { images },
            FakeKMPLogger { }
        )

        val result = manager.fetchImages()

        assertEquals(Result.success(images), result)
    }

    @Test
    fun fetchImagesError() = runTest {
        var handledException: Throwable? = null
        val exception = Exception("Images err")
        val manager = AppManager(
            FakeTimeProvider(flowOf()),
            FakeNetworkData { throw exception },
            FakeKMPLogger { handledException = it }
        )

        val result = manager.fetchImages()

        assertEquals(Result.failure(exception), result)
        assertEquals(exception, handledException)
    }

    class FakeTimeProvider(fakeTimeFlow: Flow<LocalDateTime>) : ITimeProvider {
        override val currentDateTime: Flow<LocalDateTime> = fakeTimeFlow
    }

    class FakeNetworkData(private val getFakeImages: () -> List<ImageResponse>) : INetworkData {
        override suspend fun getImages(): List<ImageResponse> = getFakeImages()
    }
}