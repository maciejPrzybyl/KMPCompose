package org.macpry.kmpcompose.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kmpcompose.composeapp.generated.resources.Res
import kmpcompose.composeapp.generated.resources.arrow_back
import org.jetbrains.compose.resources.painterResource

@Composable
fun DetailsScreen(
    state: DetailsState,
    onBack: () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        Arrangement.SpaceEvenly,
    ) {
        IconButton(
            { onBack() },
            Modifier.align(Alignment.Start)
        ) {
            Icon(
                painterResource(Res.drawable.arrow_back),
                null,
                Modifier.size(50.dp, 50.dp)
            )
        }
        Text(
            state.argValue.orEmpty(),
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}
