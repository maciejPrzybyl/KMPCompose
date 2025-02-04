package org.macpry.kmpcompose.screens.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kmpcompose.composeapp.generated.resources.Res
import kmpcompose.composeapp.generated.resources.sign_in
import org.jetbrains.compose.resources.stringResource

@Composable
fun UserScreen(
    state: UserState,
    signIn: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        state.currentUser?.let {
            Column {
                Text(it.name)
                Text(it.email)
                Text(it.photoURL.orEmpty())
            }
        } ?: TextButton(
            onClick = signIn,
        ) {
            Text(stringResource(Res.string.sign_in))
        }
    }
}
