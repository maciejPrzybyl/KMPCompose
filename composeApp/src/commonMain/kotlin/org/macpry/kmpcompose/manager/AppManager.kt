package org.macpry.kmpcompose.manager

import org.koin.core.annotation.Single
import org.macpry.kmpcompose.TimeProvider

@Single
class AppManager(
    private val timeProvider: TimeProvider
) {

    fun timeFlow() = timeProvider.currentDateTime()

}
