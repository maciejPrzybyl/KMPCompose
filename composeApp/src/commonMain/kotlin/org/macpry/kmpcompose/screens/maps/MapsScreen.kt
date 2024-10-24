package org.macpry.kmpcompose.screens.maps

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MapsScreen(
    destination: String?
) {
    Text("Map: $destination")
}