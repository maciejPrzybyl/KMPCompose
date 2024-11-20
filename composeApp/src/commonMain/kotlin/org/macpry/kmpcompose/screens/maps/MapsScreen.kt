package org.macpry.kmpcompose.screens.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun MapsScreen(
    mapsState: MapsState,
    onBack: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Map(mapsState)
        IconButton(
            { onBack() },
            Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .testTag(MapsScreenTags.BACK_BUTTON)
        ) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, null)
        }
    }
}

@Composable
expect fun Map(mapsState: MapsState)

object MapsScreenTags {
    const val BACK_BUTTON = "BACK_BUTTON"
}
