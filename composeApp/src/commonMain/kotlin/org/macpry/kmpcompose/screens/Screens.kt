package org.macpry.kmpcompose.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

//TODO Find way of coupling screen with route
sealed class BottomNavigation(/*val route: KClass<out Route>,*/ val icon: ImageVector, val label: String) {
    data object Main : BottomNavigation(/*Route.Main::class,*/ Icons.Default.AccountBox, "Main")
    data object DetailsNavArgs :
        BottomNavigation(/*Route.DetailsNavArgs::class,*/ Icons.Default.Create, "Details With Nav Args")

    data object DetailsCommonState :
        BottomNavigation(/*Route.DetailsCommonState::class,*/ Icons.Default.Add, "Details Common State")
}

fun BottomNavigation.findRoute() = when (this) {
    BottomNavigation.Main -> Route.Main::class
    BottomNavigation.DetailsNavArgs -> Route.DetailsNavArgs::class
    BottomNavigation.DetailsCommonState -> Route.DetailsCommonState::class
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
