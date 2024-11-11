import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

interface ISettingsLocalData {
    val settingsFlow: Flow<Int>
    suspend fun saveSetting(value: Int)
}

class SettingsLocalData(
    private val ioDispatcher: CoroutineDispatcher,
    private val kmpDatastore: KMPDatastore
) : ISettingsLocalData {

    override suspend fun saveSetting(value: Int): Unit = withContext(ioDispatcher) {
        kmpDatastore.setNumber(value)
    }

    override val settingsFlow = kmpDatastore.selectedNumber.flowOn(ioDispatcher)
}
