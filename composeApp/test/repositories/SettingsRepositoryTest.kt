import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsRepositoryTest {

    @Test
    fun saveSettingError() = runTest {
        var handledException: Throwable? = null
        val exception = Exception("Set err")
        val repository = SettingsRepository(
            FakeSettingsLocalData({ throw exception }, flowOf(1)),
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
            FakeSettingsLocalData({ settingToSave = it }, flowOf(2)),
            FakeKMPLogger { }
        )

        val result = repository.saveSetting(999)

        assertEquals(Result.success(Unit), result)
        assertEquals(999, settingToSave)
    }

    @Test
    fun settingsFlowSuccess() = runTest {
        val repository = SettingsRepository(
            FakeSettingsLocalData({ }, flowOf(4, 7)),
            FakeKMPLogger { }
        )

        repository.settingsFlow.test {
            assertEquals((1..SETTINGS_ITEMS_COUNT).map { it to (it == 4) }, awaitItem())
            assertEquals((1..SETTINGS_ITEMS_COUNT).map { it to (it == 7) }, awaitItem())
            awaitComplete()
        }
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
        fakeSettingsFlow: Flow<Int>
    ) : ISettingsLocalData {

        override val settingsFlow = fakeSettingsFlow

        override suspend fun saveSetting(value: Int) {
            saveSettingAction(value)
        }
    }
}
