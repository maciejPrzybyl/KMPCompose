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

@Composable
fun MapsScreen(
    destination: String?,
    onBack: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        IconButton(
            { onBack() },
            Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
        ) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, null)
        }
        Map()
    }
}

@Composable
expect fun Map()
