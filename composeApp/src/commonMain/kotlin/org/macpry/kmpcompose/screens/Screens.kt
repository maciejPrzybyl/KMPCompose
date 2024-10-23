package org.macpry.kmpcompose.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kmpcompose.composeapp.generated.resources.Res
import kmpcompose.composeapp.generated.resources.screen_details_common_state
import kmpcompose.composeapp.generated.resources.screen_details_nav_args
import kmpcompose.composeapp.generated.resources.screen_main
import kmpcompose.composeapp.generated.resources.screen_notes
import kmpcompose.composeapp.generated.resources.screen_settings
import org.jetbrains.compose.resources.StringResource

enum class BottomNavigation(val icon: ImageVector, val label: StringResource) {
    Main(Icons.Default.AccountBox, Res.string.screen_main),
    DetailsNavArgs(Icons.Default.Create, Res.string.screen_details_nav_args),
    DetailsCommonState(Icons.Default.Add, Res.string.screen_details_common_state),
    Notes(Icons.Default.Call, Res.string.screen_notes),
    Settings(Icons.Default.Settings, Res.string.screen_settings)
}
