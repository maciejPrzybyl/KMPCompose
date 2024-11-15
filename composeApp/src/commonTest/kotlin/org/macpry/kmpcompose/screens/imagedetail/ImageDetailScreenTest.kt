package org.macpry.kmpcompose.screens.imagedetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class, ExperimentalSharedTransitionApi::class)
class ImageDetailScreenTest {

    @Test
    fun displayBackButton() = runComposeUiTest {
        var onBackClicked = false
        setContent {
            SharedTransitionLayout {
                AnimatedVisibility(true) {
                    ImageDetailScreen(
                        "urrl",
                        { onBackClicked = true },
                        this@SharedTransitionLayout,
                        this@AnimatedVisibility
                    )
                }
            }
        }
        onNodeWithTag(ImageDetailScreenTags.BACK_BUTTON)
            .assertExists()
            .performClick()
        assertTrue(onBackClicked)
    }
}
