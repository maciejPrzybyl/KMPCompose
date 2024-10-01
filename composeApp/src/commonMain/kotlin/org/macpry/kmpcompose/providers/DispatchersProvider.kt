package org.macpry.kmpcompose.providers

import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

object KMPDispatchers {
    const val IO = "IODispatcher"
}

@Single
@Named(KMPDispatchers.IO)
fun provideIODispatcher() = Dispatchers.Default
