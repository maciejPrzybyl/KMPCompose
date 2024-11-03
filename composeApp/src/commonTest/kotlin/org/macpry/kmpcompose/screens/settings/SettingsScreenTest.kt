package org.macpry.kmpcompose.screens.settings

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SettingsScreenTest {

    @Test
    fun displaySettingsScreen() = runComposeUiTest {
        val state = mutableStateOf(SettingsState(listOf(4 to false, 7 to true, 8 to false)))
        setContent {
            SettingsScreen(state.value, {})
        }

        fun SemanticsNodeInteractionCollection.checkItem(
            index: Int,
            isSelected: Boolean,
            text: String
        ) {
            get(index).onChildren().let { item ->
                //item[0].run { if (isSelected) assertIsSelected() else assertIsNotSelected() }
                item[1].assertTextEquals(text)
            }
        }

        onNodeWithTag(SettingsScreenTags.ITEMS_CONTAINER, true)
            .onChildren().let {
                it.assertCountEquals(3)
                /*it.checkItem(0, false, "4")
                it.checkItem(1, true, "7")
                it.checkItem(2, false, "8")*/
            }
    }

}