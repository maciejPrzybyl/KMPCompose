package org.macpry.kmpcompose.providers

import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.IO
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

object KMPDispatchers {
    const val DEFAULT = "DefaultDispatcher"
    const val IO = "IODispatcher"
}

@Single
@Named(KMPDispatchers.DEFAULT)
fun provideDefaultDispatcher() = Dispatchers.Default

@Single
@Named(KMPDispatchers.IO)
@Deprecated(
    "Cannot import kotlinx.coroutines.IO because lack of wasm implementation",
    ReplaceWith("KMPDispatchers.DEFAULT", "org.macpry.kmpcompose.providers")
)

//TODO Should provide platform-specific implementation to fix lack of wasm support, but there are issues with injecting this
fun provideIODispatcher() = Dispatchers.Default
