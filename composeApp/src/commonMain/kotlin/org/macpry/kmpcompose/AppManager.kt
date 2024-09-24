package org.macpry.kmpcompose

class AppManager(
    private val timeProvider: TimeProvider
) {

    fun timeFlow() = timeProvider.currentDateTime()

}
