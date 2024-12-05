package org.macpry.kmpcompose.logger

interface IKMPLogger {
    fun logError(exception: Throwable)
}

class KMPLogger : IKMPLogger {
    override fun logError(exception: Throwable) {
        println(exception)
    }
}
