package org.macpry.kmpcompose.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmpcompose.composeapp.generated.resources.Res
import kmpcompose.composeapp.generated.resources.compose_multiplatform
import kmpcompose.composeapp.generated.resources.title_details
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.macpry.kmpcompose.Greeting
import org.macpry.kmpcompose.screens.MainState

@Composable
fun MainScreen(
    state: MainState,
    onOpenDetails: (arg: String) -> Unit
) {
    val greeting = remember { Greeting().greet() }
    Column(
        Modifier.fillMaxSize(),
        Arrangement.SpaceEvenly,
        Alignment.CenterHorizontally
    ) {
        Text("Compose: $greeting")
        Text(state.currentTime.toString())

        var inputText by remember { mutableStateOf("") }
        TextField(
            inputText,
            { inputText = it }
        )
        TextButton({
            onOpenDetails(inputText)
        }) {
            Text(stringResource(Res.string.title_details))
        }
        val pagerState = rememberPagerState(pageCount = { 11 })
        HorizontalPager(pagerState) { page ->
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource(Res.drawable.compose_multiplatform),
                    null,
                    Modifier.width(200.dp)
                )
                Text("Page: $page")
            }
        }
    }
}
