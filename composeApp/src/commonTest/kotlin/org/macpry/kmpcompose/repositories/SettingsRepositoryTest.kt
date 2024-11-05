package org.macpry.kmpcompose.repositories

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.macpry.kmpcompose.data.local.ISettingsLocalData
import org.macpry.kmpcompose.logger.FakeKMPLogger
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun saveSettingError() = runTest {
        var handledException: Throwable? = null
        val exception = Exception("Set err")
        val repository = SettingsRepository(
            FakeSettingsLocalData({ throw exception }, MutableStateFlow(emptyList())),
            FakeKMPLogger { handledException = it }
        )

        val result = repository.saveSetting(643)

        assertEquals(Result.failure(exception), result)
        assertEquals(exception, handledException)
    }

    @Test
    fun saveSettingSuccess() = runTest {
        var settingToSave: Int? = null
        val repository = SettingsRepository(
            FakeSettingsLocalData({ settingToSave = it }, MutableStateFlow(emptyList())),
            FakeKMPLogger { }
        )

        val result = repository.saveSetting(999)

        assertEquals(Result.success(Unit), result)
        assertEquals(999, settingToSave)
    }

    @Test
    fun settingsFlowError() = runTest {
        var handledException: Throwable? = null
        val exception = Exception("Set flo err")
        val repository = SettingsRepository(
            FakeSettingsLocalData({ }, flow { throw exception }),
            FakeKMPLogger { handledException = it }
        )

        repository.settingsFlow.test {
            assertEquals(exception, handledException)
            awaitComplete()
        }
    }

    class FakeSettingsLocalData(
        private val saveSettingAction: (Int) -> Unit,
        fakeSettingsFlow: Flow<List<Pair<Int, Boolean>>>
    ) : ISettingsLocalData {

        override val settingsFlow = fakeSettingsFlow

        override suspend fun saveSetting(value: Int) {
            saveSettingAction(value)
        }
    }
}
