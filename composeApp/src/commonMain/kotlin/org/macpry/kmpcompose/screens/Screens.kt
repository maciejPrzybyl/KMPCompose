package org.macpry.kmpcompose.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kmpcompose.composeapp.generated.resources.Res
import kmpcompose.composeapp.generated.resources.home_navigation_main
import kmpcompose.composeapp.generated.resources.home_navigation_notes
import kmpcompose.composeapp.generated.resources.home_navigation_settings
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import org.macpry.kmpcompose.screens.maps.model.Coordinates

@Serializable
sealed class AppNavigationRoutes {
    @Serializable
    data object Home : AppNavigationRoutes()

    @Serializable
    data class Maps(val coordinates: Coordinates) : AppNavigationRoutes()

    @Serializable
    data class ImageDetail(val url: String) : AppNavigationRoutes()
}

enum class HomeBottomNavigation(val icon: ImageVector, val label: StringResource) {
    Main(Icons.Default.AccountBox, Res.string.home_navigation_main),
    Notes(Icons.Default.Call, Res.string.home_navigation_notes),
    Settings(Icons.Default.Settings, Res.string.home_navigation_settings)
}
