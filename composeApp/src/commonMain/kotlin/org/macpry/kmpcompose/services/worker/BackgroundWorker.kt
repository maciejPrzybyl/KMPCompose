package org.macpry.kmpcompose.services.worker

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

abstract class BackgroundWorker {
    abstract fun start()
    abstract val progressFlow: Flow<Int>
    abstract val tag: String

    companion object {
        const val PROGRESS_TAG = "PROGRESS"
    }
}

class DummyBackgroundWorker : BackgroundWorker() {
    override fun start() {}

    override val progressFlow: Flow<Int> = flowOf(10)
    override val tag: String = "DummyBackgroundWorker"
}
