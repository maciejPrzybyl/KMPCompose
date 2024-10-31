package org.macpry.kmpcompose.screens.notes

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class NotesScreenTest {

    @Test
    fun displayNotesScreen() = runComposeUiTest {
        val state = mutableStateOf(NotesState(listOf("n1")))
        setContent {
            NotesScreen(state.value, { })
        }
        onNodeWithTag(NotesScreenTags.NOTE_INPUT, true)
            .assertIsNotFocused()
            .assertTextEquals("")
        onNodeWithTag(NotesScreenTags.DONE_BUTTON)
            .assertIsNotEnabled()
            .assertTextEquals("Save")
        onNodeWithTag(NotesScreenTags.NOTES_LIST)
            .onChildren().let {
                it.assertCountEquals(1)
                it[0].onChild().assertTextEquals("n1")
            }
    }

    @Test
    fun saveNoteOnButtonClick() = runComposeUiTest {
        val state = mutableStateOf(NotesState(listOf("n1")))
        var textToSave: String? = null
        setContent {
            NotesScreen(state.value, { textToSave = it })
        }

        onNodeWithTag(NotesScreenTags.NOTE_INPUT).let {
            it.performTextInput("n2")
            it.assertIsFocused()
        }
        onNodeWithTag(NotesScreenTags.DONE_BUTTON)
            .assertIsEnabled()
            .performClick()
        onNodeWithTag(NotesScreenTags.NOTE_INPUT, true)
            .assertIsNotFocused()
            .assertTextEquals("")
        assertEquals("n2", textToSave)
        state.value = NotesState(listOf("n1", "n2"))
        onNodeWithTag(NotesScreenTags.NOTES_LIST)
            .onChildren().let {
                it.assertCountEquals(2)
                it[0].onChild().assertTextEquals("n1")
                it[1].onChild().assertTextEquals("n2")
            }
    }

    @Test
    fun saveNoteOnImeDone() = runComposeUiTest {
        var textToSave: String? = null
        setContent {
            NotesScreen(NotesState(emptyList()), { textToSave = it })
        }

        onNodeWithTag(NotesScreenTags.NOTE_INPUT).let {
            it.performTextInput("n3")
            it.performImeAction()
        }
        assertEquals("n3", textToSave)
    }

}