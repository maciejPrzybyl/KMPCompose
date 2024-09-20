package org.macpry.kmpcompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kmpcompose.composeapp.generated.resources.Res
import kmpcompose.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.macpry.kmpcompose.di.koinConfiguration

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

@Composable
fun MainScreen(appViewModel: AppViewModel = koinViewModel()) {
    val state by appViewModel.currentTime.collectAsStateWithLifecycle()
    val greeting = remember { Greeting().greet() }
    Text("Compose: $greeting")
    Text(state.toString().orEmpty())

    var inputText by remember { mutableStateOf("") }
    TextField(
        inputText,
        { inputText = it }
    )
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
