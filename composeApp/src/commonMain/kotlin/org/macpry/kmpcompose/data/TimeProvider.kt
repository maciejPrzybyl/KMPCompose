package org.macpry.kmpcompose.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.macpry.kmpcompose.providers.KMPDispatchers
import kotlin.time.Duration.Companion.seconds

@Single
class TimeProvider(
    @Named(KMPDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {

    internal fun currentDateTime() = flow {
        while (true) {
            emit(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
            delay(1.seconds)
        }
    }.flowOn(ioDispatcher)

}
