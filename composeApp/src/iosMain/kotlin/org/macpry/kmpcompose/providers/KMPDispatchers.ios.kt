package org.macpry.kmpcompose.providers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual fun provideIODispatcher() = Dispatchers.IO
