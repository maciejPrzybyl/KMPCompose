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
    "Cannot import kotlinx.coroutines.IO",
    ReplaceWith("KMPDispatchers.DEFAULT", "org.macpry.kmpcompose.providers")
)

//TODO Investigate why cannot import kotlinx.coroutines.IO - it's not event included in sources
fun provideIODispatcher() = Dispatchers.Default
