package org.macpry.kmpcompose.providers

import kotlinx.coroutines.Dispatchers

object KMPDispatchers {
    const val DEFAULT = "DefaultDispatcher"
    const val IO = "IODispatcher"
}

fun provideDefaultDispatcher() = Dispatchers.Default

@Deprecated(
    "Cannot import kotlinx.coroutines.IO because lack of wasm implementation",
    ReplaceWith("KMPDispatchers.DEFAULT", "org.macpry.kmpcompose.providers")
)

//TODO Should provide platform-specific implementation to fix lack of wasm support, but there are issues with injecting this
fun provideIODispatcher() = Dispatchers.Default
