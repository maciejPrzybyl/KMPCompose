package org.macpry.kmpcompose.services.worker

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.macpry.kmpcompose.services.worker.BackgroundWorker.Companion.MAX_PROGRESS
import kotlin.time.Duration.Companion.seconds

abstract class BackgroundWorker {
    abstract fun start()
    abstract val progressFlow: Flow<Int>
    abstract val tag: String

    companion object {
        const val PROGRESS_TAG = "PROGRESS"
        const val MAX_PROGRESS = 100
        const val title = "Count"
        const val contentText = "Counting progress"
    }
}

suspend fun count(
    steps: Int = 10,
    onEach: (Int) -> Unit,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    try {
        (0..MAX_PROGRESS).step(steps).forEach {
            onEach(it)
            delay(1.seconds)
        }
        onSuccess()
    } catch (exception: Exception) {
        println(exception)
        onFailure(exception)
    }
}

class DummyBackgroundWorker : BackgroundWorker() {
    override fun start() {}

    override val progressFlow: Flow<Int> = flowOf(10)
    override val tag: String = "DummyBackgroundWorker"
}
