package org.macpry.kmpcompose.screens.main

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MainScreenTest {

    @Test
    fun displayCurrentTime() = runComposeUiTest {
        val currentTime = mutableStateOf<String?>("21:37")
        setContent {
            MainScreen(MainState(currentTime.value), emptyList(), {})
        }

        onNodeWithTag(MainScreenTags.CURRENT_TIME_TEXT).run {
            assertTextEquals("21:37")
            currentTime.value = null
            assertTextEquals("")
        }
    }

    @Test
    fun displayMainScreen() = runComposeUiTest {
        setContent {
            MainScreen(MainState(null), emptyList(), {})
        }

        onNodeWithTag(MainScreenTags.OPEN_MAPS_BUTTON).run {
            assertIsEnabled()
            onNodeWithTag(MainScreenTags.COORDINATE_INPUT_LAT).performTextInput("100")
            assertIsNotEnabled()
        }
    }
}