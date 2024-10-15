package org.macpry.kmpcompose.providers

import kotlinx.coroutines.Dispatchers

actual fun provideIODispatcher() = Dispatchers.IO
