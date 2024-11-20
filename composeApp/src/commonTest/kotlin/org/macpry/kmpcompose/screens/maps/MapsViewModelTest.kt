package org.macpry.kmpcompose.screens.maps

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.macpry.kmpcompose.screens.maps.model.Coordinates
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MapsViewModelTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun setLocation() = runTest {
        val viewModel = MapsViewModel()

        viewModel.mapsState.test {
            assertEquals(MapsState(), awaitItem())

            val coordinates = Coordinates(-21.4, 83.3)
            viewModel.setLocation(coordinates)
            assertEquals(MapsState(coordinates, false), awaitItem())
        }
    }
}