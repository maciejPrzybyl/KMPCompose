package org.macpry.kmpcompose.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun DetailsScreen(
    state: DetailsState
) {
    Column(
        Modifier.fillMaxSize(),
        Arrangement.SpaceEvenly,
    ) {
        Text(
            state.argValue.orEmpty(),
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}
