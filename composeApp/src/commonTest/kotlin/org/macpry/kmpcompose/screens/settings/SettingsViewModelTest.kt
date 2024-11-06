package org.macpry.kmpcompose.screens.settings

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.macpry.kmpcompose.repositories.ISettingsRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun observeSettingsState() = runTest {
        val settingsFlow = MutableStateFlow<List<Pair<Int, Boolean>>>(emptyList())
        val viewModel = SettingsViewModel(FakeSettingsRepository(null, settingsFlow))

        viewModel.settingsState.test {
            assertEquals(SettingsState(emptyList()), awaitItem())

            val settings = listOf(1 to false, 2 to true)
            settingsFlow.emit(settings)
            assertEquals(SettingsState(settings), awaitItem())
        }
    }

    @Test
    fun saveSettingError() = runTest {
        val exception = Exception("Save error")
        val viewModel =
            SettingsViewModel(FakeSettingsRepository(exception, flowOf(emptyList())))

        viewModel.error.test {
            viewModel.saveSetting(666)
            assertEquals(exception, awaitItem())
        }
    }

    @Test
    fun saveSettingSuccess() = runTest {
        val viewModel =
            SettingsViewModel(FakeSettingsRepository(null, flowOf(emptyList())))

        viewModel.error.test {
            viewModel.saveSetting(333)
            expectNoEvents()
        }
    }

    class FakeSettingsRepository(
        private val saveResultException: Exception?,
        fakeSettingsFlow: Flow<List<Pair<Int, Boolean>>>
    ) : ISettingsRepository {
        override suspend fun saveSetting(value: Int) =
            saveResultException?.let { Result.failure(it) } ?: Result.success(Unit)

        override val settingsFlow: Flow<List<Pair<Int, Boolean>>> = fakeSettingsFlow
    }
}