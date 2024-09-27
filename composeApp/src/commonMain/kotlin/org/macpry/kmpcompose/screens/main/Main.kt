package org.macpry.kmpcompose.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kmpcompose.composeapp.generated.resources.Res
import kmpcompose.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.macpry.kmpcompose.Greeting
import org.macpry.kmpcompose.screens.MainState

@Composable
fun MainScreen(
    state: MainState,
    navArgsInputText: String,
    navArgsOnTextChanged: (String) -> Unit,
    navArgsOnOpenDetails: () -> Unit,
    commonOnTextChanged: (String) -> Unit,
    commonOnOpenDetails: () -> Unit
) {
    val greeting = remember { Greeting().greet() }
    Column(
        Modifier.fillMaxSize(),
        Arrangement.SpaceEvenly,
        Alignment.CenterHorizontally
    ) {
        Text("Compose: $greeting")
        Spacer(Modifier.height(400.dp))
        Text(state.currentTime.toString())

        Row(
            //horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.height(50.dp).fillMaxWidth(),
        ) {
            TextField(
                value = navArgsInputText,
                onValueChange = { navArgsOnTextChanged(it) },
                label = { Text("Input passed by nav args") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { navArgsOnOpenDetails() }
                ),
                singleLine = true
            )
            //Spacer(Modifier.width(100.dp))
            TextField(
                value = state.inputText.orEmpty(),
                onValueChange = { commonOnTextChanged(it) },
                label = { Text("Input passed by view model") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { commonOnOpenDetails() }
                ),
                singleLine = true
            )
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
