package org.macpry.kmpcompose.managers

import org.koin.core.annotation.Single

@Single
class AppManager(
    private val timeProvider: TimeProvider
) {

    fun timeFlow() = timeProvider.currentDateTime()

}
