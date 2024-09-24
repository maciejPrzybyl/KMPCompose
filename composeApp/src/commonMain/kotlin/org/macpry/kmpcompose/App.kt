package org.macpry.kmpcompose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.macpry.kmpcompose.di.koinConfiguration
import org.macpry.kmpcompose.screens.main.MainScreen

@Composable
@Preview
fun App() {
    KoinApplication(::koinConfiguration) {
        MaterialTheme {
            Column(
                Modifier.fillMaxSize(),
                Arrangement.SpaceEvenly,
                Alignment.CenterHorizontally
            ) {
                MainScreen()
            }
        }
    }
}
