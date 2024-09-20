package org.macpry.kmpcompose

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.seconds

@Inject
class TimeManager(
    private val timeProvider: TimeProvider
) {

    fun timeFlow() = flow {
        emit(timeProvider.getCurrentDateTime())
        delay(1.seconds)
    }

}
