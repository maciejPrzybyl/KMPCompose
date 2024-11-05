package org.macpry.kmpcompose.screens.maps

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import org.macpry.kmpcompose.screens.maps.model.Coordinates
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class MapsScreenTest {

    @Test
    fun displayBackButton() = runComposeUiTest {
        var onBackClicked = false
        setContent {
            MapsScreen(Coordinates(1.2, 3.4), { onBackClicked = true })
        }
        onNodeWithTag(MapsScreenTags.BACK_BUTTON)
            .assertExists()
            .performClick()
        assertTrue(onBackClicked)
    }
}
