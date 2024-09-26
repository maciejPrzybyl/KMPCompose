package org.macpry.kmpcompose.screens

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Main : Screen()

    @Serializable
    data class Details(val arg: String) : Screen()
}
