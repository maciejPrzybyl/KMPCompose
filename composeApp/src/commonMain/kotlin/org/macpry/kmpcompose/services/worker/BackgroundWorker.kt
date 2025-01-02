package org.macpry.kmpcompose.services.worker

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

abstract class BackgroundWorker {
    abstract suspend fun start()
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

class DummyBackgroundWorker : BackgroundWorker() {
    override suspend fun start() {}

    override val progressFlow: Flow<Int> = flowOf(10)
    override val tag: String = "DummyBackgroundWorker"
}
