package org.macpry.kmpcompose.managers

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.seconds

@Single
class TimeProvider {

    fun currentDateTime() = flow {
        while (true) {
            emit(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
            delay(1.seconds)
        }
    }
    //TODO Inject IO dispatcher

}
