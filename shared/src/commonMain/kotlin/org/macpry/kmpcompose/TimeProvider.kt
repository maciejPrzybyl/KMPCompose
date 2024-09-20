package org.macpry.kmpcompose

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Inject

@Inject
class TimeProvider {

    fun getCurrentDateTime() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

}
