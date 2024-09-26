package org.macpry.kmpcompose.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun DetailsNavArgsScreen(
    argValue: String?
) {
    Column(
        Modifier.fillMaxSize(),
        Arrangement.SpaceEvenly,
    ) {
        Text(
            argValue.orEmpty(),
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}
