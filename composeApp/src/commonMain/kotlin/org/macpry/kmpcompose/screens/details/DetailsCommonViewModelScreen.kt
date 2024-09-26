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
fun DetailsCommonViewModelScreen(
    inputText: String?
) {
    Column(
        Modifier.fillMaxSize(),
        Arrangement.SpaceEvenly,
    ) {
        Text(
            inputText.orEmpty(),
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}
