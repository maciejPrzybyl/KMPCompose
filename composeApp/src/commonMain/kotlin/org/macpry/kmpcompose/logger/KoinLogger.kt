package org.macpry.kmpcompose.logger

import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE

class KoinLogger: Logger(level = Level.DEBUG) {
    override fun display(level: Level, msg: MESSAGE) {
        //println(msg)
    }
}