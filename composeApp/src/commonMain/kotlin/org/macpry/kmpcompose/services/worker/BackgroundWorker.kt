package org.macpry.kmpcompose.services.worker

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import org.macpry.kmpcompose.services.worker.BackgroundWorker.Companion.range
import kotlin.time.Duration.Companion.seconds

abstract class BackgroundWorker {
    abstract fun start()
    abstract val progressFlow: Flow<Int>
    abstract val tag: String

    companion object {
        const val PROGRESS_TAG = "PROGRESS"
        const val MAX_PROGRESS = 100
        const val STEPS = 10
        const val NOTIFICATION_TITLE = "Count"
        const val NOTIFICATION_CONTENT = "Counting progress"

        val range get() = (0..MAX_PROGRESS).step(STEPS)
    }
}

suspend fun count(
    ioDispatcher: CoroutineDispatcher,
    onInit: suspend CoroutineScope.() -> Unit,
    onEach: suspend CoroutineScope.(Int) -> Unit
): Result<Unit> = withContext(ioDispatcher) {
    onInit()
    runCatching {
        range.forEach {
            onEach(it)
            delay(1.seconds)
        }
        return@withContext Result.success(Unit)
    }
}

class DummyBackgroundWorker : BackgroundWorker() {
    override fun start() {}
    override val progressFlow: Flow<Int> = flowOf(10)
    override val tag: String = "DummyBackgroundWorker"
}
