package org.macpry.kmpcompose.providers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object KMPDispatchers {
    const val DEFAULT = "DefaultDispatcher"
    const val IO = "IODispatcher"
}

fun provideDefaultDispatcher() = Dispatchers.Default

expect fun provideIODispatcher(): CoroutineDispatcher
