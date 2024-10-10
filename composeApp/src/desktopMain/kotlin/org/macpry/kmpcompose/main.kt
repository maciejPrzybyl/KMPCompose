package org.macpry.kmpcompose

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.macpry.kmpcompose.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMPCompose",
    ) {
        App()
    }
}