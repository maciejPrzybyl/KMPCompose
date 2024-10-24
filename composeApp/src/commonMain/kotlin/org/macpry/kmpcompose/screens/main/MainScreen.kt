package org.macpry.kmpcompose.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import kmpcompose.composeapp.generated.resources.Res
import kmpcompose.composeapp.generated.resources.compose_multiplatform
import kmpcompose.composeapp.generated.resources.destination_input_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.macpry.kmpcompose.Greeting
import org.macpry.kmpcompose.data.network.NetworkData
import org.macpry.kmpcompose.screens.MainState

@Composable
fun MainScreen(
    state: MainState,
    images: List<NetworkData.ImageResponse>,
    onDestinationInputTextChanged: (String) -> Unit,
    onDestinationPicked: () -> Unit
) {
    val greeting = remember { Greeting().greet() }
    Column(
        Modifier.fillMaxSize(),
        Arrangement.SpaceEvenly,
        Alignment.CenterHorizontally
    ) {
        Text("Compose: $greeting")
        Text(state.currentTime.toString())
        Spacer(Modifier.height(12.dp))
        TextField(
            value = state.destination.orEmpty(),
            onValueChange = { onDestinationInputTextChanged(it) },
            label = { Text(stringResource(Res.string.destination_input_label)) },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { onDestinationPicked() }
            ),
            singleLine = true
        )
        PagerWithIndicator(images)
    }
}

@Composable
fun PagerWithIndicator(images: List<NetworkData.ImageResponse>) {
    val pagerState = rememberPagerState { images.size }
    HorizontalPager(pagerState) { page ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageData = images[page]
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(imageData.url)
                    .build(),
                contentDescription = null,
                modifier = Modifier.aspectRatio(ratio = 1.0f),
                placeholder = painterResource(Res.drawable.compose_multiplatform)
            )
            Text("${imageData.id} ${imageData.author}")
        }
    }
    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(16.dp)
            )
        }
    }
}
