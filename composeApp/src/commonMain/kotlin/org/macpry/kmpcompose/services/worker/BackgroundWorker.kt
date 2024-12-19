package org.macpry.kmpcompose.services.worker

import kotlinx.coroutines.flow.Flow

abstract class BackgroundWorker {
    abstract fun start()
    abstract val progressFlow: Flow<Int>
    abstract val tag: String

    companion object {
        const val PROGRESS = "PROGRESS"
    }
}
