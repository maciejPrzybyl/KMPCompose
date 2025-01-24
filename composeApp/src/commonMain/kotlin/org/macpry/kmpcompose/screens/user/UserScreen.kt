package org.macpry.kmpcompose.screens.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun UserScreen(
    state: UserState,
    signIn: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        TextButton(
            onClick = signIn,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("Sign in")
        }
    }
}
