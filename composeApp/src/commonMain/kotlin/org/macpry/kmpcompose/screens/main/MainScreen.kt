@file:OptIn(ExperimentalSharedTransitionApi::class)

package org.macpry.kmpcompose.screens.main

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import kmpcompose.composeapp.generated.resources.Res
import kmpcompose.composeapp.generated.resources.compose_multiplatform
import kmpcompose.composeapp.generated.resources.coordinates_input_latitude
import kmpcompose.composeapp.generated.resources.coordinates_input_longitude
import kmpcompose.composeapp.generated.resources.images_load_error
import kmpcompose.composeapp.generated.resources.open_maps
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.macpry.kmpcompose.Greeting
import org.macpry.kmpcompose.data.network.ImageResponse
import org.macpry.kmpcompose.ui.colorId

val latRange = -90.0..90.0
val lngRange = -180.0..180.0

@Composable
fun MainScreen(
    state: MainState,
    onOpenMaps: (Pair<Double, Double>) -> Unit,
    onOpenImageDetails: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope?,
    animatedVisibilityScope: AnimatedVisibilityScope?
) {
    val greeting = remember { Greeting().greet() }
    Column(
        Modifier.fillMaxSize(),
        Arrangement.SpaceEvenly,
        Alignment.CenterHorizontally
    ) {
        Text("Compose: $greeting")
        Text(
            text = state.currentTime.orEmpty(),
            modifier = Modifier.testTag(MainScreenTags.CURRENT_TIME_TEXT)
        )
        Spacer(Modifier.height(12.dp))
        CoordinatesView(onOpenMaps)
        PagerContainer(
            state.imagesState,
            onOpenImageDetails,
            sharedTransitionScope,
            animatedVisibilityScope
        )
    }
}

@Composable
fun CoordinatesView(
    onOpenMaps: (Pair<Double, Double>) -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        var latLng by rememberSaveable { mutableStateOf(0.0 to 0.0) }
        fun areCoordinatesValid() = latLng.first in latRange && latLng.second in lngRange

        fun tryToOpenMaps() {
            if (areCoordinatesValid()) {
                onOpenMaps(latLng.first to latLng.second)
            }
        }

        CoordinateInput(
            modifier = Modifier
                .weight(0.4f)
                .testTag(MainScreenTags.COORDINATE_INPUT_LAT),
            value = latLng.first,
            onChange = { latLng = latLng.copy(first = it) },
            label = Res.string.coordinates_input_latitude,
            onDone = { tryToOpenMaps() }
        )
        CoordinateInput(
            modifier = Modifier.weight(0.4f),
            value = latLng.second,
            onChange = { latLng = latLng.copy(second = it) },
            label = Res.string.coordinates_input_longitude,
            onDone = { tryToOpenMaps() }
        )
        TextButton(
            modifier = Modifier
                .weight(0.2f)
                .testTag(MainScreenTags.OPEN_MAPS_BUTTON),
            onClick = {
                tryToOpenMaps()
            },
            enabled = areCoordinatesValid()
        ) {
            Text(stringResource(Res.string.open_maps))
        }
    }
}

@Composable
fun CoordinateInput(
    modifier: Modifier,
    value: Double,
    onChange: (Double) -> Unit,
    label: StringResource,
    onDone: () -> Unit
) {
    TextField(
        modifier = modifier,
        value = value.toString(),
        onValueChange = { onChange(it.toDoubleOrNull() ?: 0.0) },
        label = { Text(stringResource(label)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        ),
        singleLine = true
    )
}

@Composable
fun PagerContainer(
    imagesState: ImagesState,
    onOpenImageDetails: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope?,
    animatedVisibilityScope: AnimatedVisibilityScope?
) {
    when (imagesState) {
        ImagesState.Init -> PagerWithIndicator(
            listOf(ImageResponse(0, "", "unknown")),
            {},
            sharedTransitionScope,
            animatedVisibilityScope
        )

        ImagesState.Loading -> CircularProgressIndicator(
            Modifier.testTag(MainScreenTags.PAGER_LOADING)
        )

        is ImagesState.Error -> Text(
            text = stringResource(Res.string.images_load_error),
            modifier = Modifier.testTag(MainScreenTags.PAGER_ERROR)
        )

        is ImagesState.Success -> PagerWithIndicator(
            imagesState.images,
            onOpenImageDetails,
            sharedTransitionScope,
            animatedVisibilityScope
        )
    }
}

@Composable
fun PagerWithIndicator(
    images: List<ImageResponse>,
    onOpenImageDetails: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope?,
    animatedVisibilityScope: AnimatedVisibilityScope?
) {
    val pagerState = rememberPagerState { images.size }
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.testTag(MainScreenTags.PAGER)
    ) { page ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageData = images[page]
            sharedTransitionScope?.run {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(imageData.url)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(ratio = 1.0f)
                        .clickable {
                            onOpenImageDetails(imageData.url)
                        }.apply {
                            animatedVisibilityScope?.let {
                                sharedElement(
                                    state = rememberSharedContentState("image${imageData.url}"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                            }
                        }
                        .testTag(MainScreenTags.PAGER_ITEM_IMAGE),
                    placeholder = painterResource(Res.drawable.compose_multiplatform)
                )
            }
            Text(
                text = "${imageData.id} ${imageData.author}",
                modifier = Modifier.testTag(MainScreenTags.PAGER_ITEM_DESCRIPTION)
            )
        }
    }
    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .testTag(MainScreenTags.PAGER_INDICATOR),
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
                    .testTag(MainScreenTags.PAGER_INDICATOR_ITEM)
                    .semantics {
                        colorId = color
                    }
            )
        }
    }
}

object MainScreenTags {
    const val CURRENT_TIME_TEXT = "CURRENT_TIME_TEXT"
    const val OPEN_MAPS_BUTTON = "OPEN_MAPS_BUTTON"
    const val COORDINATE_INPUT_LAT = "COORDINATES_INPUT_LAT"
    const val PAGER_LOADING = "PAGER_LOADING"
    const val PAGER_ERROR = "PAGER_ERROR"
    const val PAGER = "PAGER"
    const val PAGER_ITEM_IMAGE = "PAGER_ITEM_IMAGE"
    const val PAGER_ITEM_DESCRIPTION = "PAGER_ITEM_DESCRIPTION"
    const val PAGER_INDICATOR = "PAGER_INDICATOR"
    const val PAGER_INDICATOR_ITEM = "PAGER_INDICATOR_ITEM"
}
