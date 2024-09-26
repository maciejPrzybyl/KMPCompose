package org.macpry.kmpcompose.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Create
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

//TODO Find better way of coupling screen with route
sealed class Screen(val route: KClass<out Route>, val icon: ImageVector, val label: String) {
    data object Main : Screen(Route.Main::class, Icons.Default.AccountBox, "Main")
    data object Details : Screen(Route.Details::class, Icons.Default.Create, "Details")
}

@Serializable
sealed class Route {
    @Serializable
    data object Main : Route()

    @Serializable
    data class Details(val arg: String) : Route()
}
