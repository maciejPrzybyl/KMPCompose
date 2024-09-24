package org.macpry.kmpcompose

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.seconds

class TimeProvider {

    fun currentDateTime() = flow {
        while (true) {
            emit(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
            delay(1.seconds)
        }
    }

}
