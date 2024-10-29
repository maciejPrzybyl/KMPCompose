package org.macpry.kmpcompose.screens.main

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MainScreenTest {

    @Test
    fun displayMainScreen() = runComposeUiTest {
        setContent {
            MainScreen(MainState("21:37"), emptyList(), {})
        }

        onNodeWithTag(MainScreenTags.CURRENT_TIME_TEXT).assertTextEquals("21:37")
    }
}