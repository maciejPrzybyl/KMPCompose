package org.macpry.kmpcompose.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

//TODO Find better way of coupling screen with route
sealed class Screen(val route: KClass<out Route>, val icon: ImageVector, val label: String) {
    data object Main : Screen(Route.Main::class, Icons.Default.AccountBox, "Main")
    data object DetailsNavArgs : Screen(Route.DetailsNavArgs::class, Icons.Default.Create, "Details With Nav Args")
    data object DetailsCommonState : Screen(Route.DetailsCommonState::class, Icons.Default.Add, "Details Common State")
}

@Serializable
sealed class Route {
    @Serializable
    data object Main : Route()

    @Serializable
    data class DetailsNavArgs(val arg: String) : Route()

    @Serializable
    data object DetailsCommonState : Route()
}
