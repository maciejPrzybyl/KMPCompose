package org.macpry.kmpcompose.screens.main

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import org.macpry.kmpcompose.data.network.ImageResponse
import org.macpry.kmpcompose.extensions.assertColorEquals
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class MainScreenTest {

    @Test
    fun displayCurrentTime() = runComposeUiTest {
        val currentTime = mutableStateOf<String?>("21:37")
        setContent {
            MainScreen(MainState(currentTime.value, ImagesState.Init), {})
        }

        onNodeWithTag(MainScreenTags.CURRENT_TIME_TEXT).run {
            assertTextEquals("21:37")
            currentTime.value = null
            assertTextEquals("")
        }
    }

    @Test
    fun displayMapsButton() = runComposeUiTest {
        var onOpenMapsClick = 0.0 to 0.0
        setContent {
            MainScreen(MainState(null, ImagesState.Init), { onOpenMapsClick = it })
        }

        onNodeWithTag(MainScreenTags.OPEN_MAPS_BUTTON).run {
            assertIsEnabled()
            assertTextEquals("Open in maps")

            onNodeWithTag(MainScreenTags.COORDINATE_INPUT_LAT).performTextInput("100")
            assertIsNotEnabled()

            onNodeWithTag(MainScreenTags.COORDINATE_INPUT_LAT).run {
                performTextClearance()
                performTextInput("2")
            }
            performClick()
            assertEquals(20.0 to 0.0, onOpenMapsClick)

            /*onNodeWithTag(MainScreenTags.COORDINATE_INPUT_LAT).run {
                performTextClearance()
                performTextInput("3")
                performImeAction()
            }
            assertEquals(30.0 to 0.0, onOpenMapsClick)*/
        }
    }

    @Test
    fun displayPagerContainer() = runComposeUiTest {
        val imagesState = mutableStateOf<ImagesState>(ImagesState.Init)

        setContent {
            PagerContainer(imagesState.value)
        }

        onNodeWithTag(MainScreenTags.PAGER_LOADING).assertDoesNotExist()
        onNodeWithTag(MainScreenTags.PAGER_ERROR).assertDoesNotExist()
        onNodeWithTag(MainScreenTags.PAGER).assertExists()
        onNodeWithTag(MainScreenTags.PAGER_ITEM_DESCRIPTION)
            .assertExists()
            .assertTextEquals("0 unknown")

        imagesState.value = ImagesState.Loading
        onNodeWithTag(MainScreenTags.PAGER).assertDoesNotExist()
        onNodeWithTag(MainScreenTags.PAGER_ERROR).assertDoesNotExist()
        onNodeWithTag(MainScreenTags.PAGER_LOADING).assertExists()

        imagesState.value = ImagesState.Error(Exception("Loading failed"))
        onNodeWithTag(MainScreenTags.PAGER).assertDoesNotExist()
        onNodeWithTag(MainScreenTags.PAGER_LOADING).assertDoesNotExist()
        onNodeWithTag(MainScreenTags.PAGER_ERROR)
            .assertExists()
            .assertTextEquals("Couldn't load images")
    }

    @Test
    fun displayPager() = runComposeUiTest {
        val images = (1L..2).map {
            ImageResponse(it, "$it url", "$it aut")
        }
        setContent {
            MainScreen(MainState(null, ImagesState.Success(images)), { })
        }

        onNodeWithTag(MainScreenTags.PAGER).assertExists()
        onNodeWithTag(MainScreenTags.PAGER_LOADING).assertDoesNotExist()
        onAllNodesWithTag(MainScreenTags.PAGER_ITEM_DESCRIPTION)
            .onFirst()
            .assertTextEquals("${images.first().id} ${images.first().author}")
    }

    @Test
    fun displayPagerIndicator() = runComposeUiTest {
        val images = (1L..5).map {
            ImageResponse(it, "$it urlaa", "$it authoo")
        }
        setContent {
            MainScreen(MainState(null, ImagesState.Success(images)), { })
        }

        fun checkIndicators(currentItemIndex: Int) {
            onAllNodesWithTag(MainScreenTags.PAGER_INDICATOR_ITEM).let { items ->
                images.forEachIndexed { index, _ ->
                    items[index].assertColorEquals(if (index == currentItemIndex) Color.DarkGray else Color.LightGray)
                }
            }
        }

        onNodeWithTag(MainScreenTags.PAGER_INDICATOR).onChildren().assertCountEquals(5)
        checkIndicators(0)
        onNodeWithTag(MainScreenTags.PAGER).performScrollToIndex(3)
        checkIndicators(3)
    }
}
