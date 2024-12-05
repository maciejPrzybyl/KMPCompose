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
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class NotesScreenTest {

    @Test
    fun displayNotesScreen() = runComposeUiTest {
        val state = mutableStateOf(NotesState(listOf("n1")))
        setContent {
            NotesScreen(state.value, mutableStateOf("" to false), {}, {})
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

        state.value = NotesState(listOf("n1", "n2"))
        onNodeWithTag(NotesScreenTags.NOTES_LIST)
            .onChildren().let {
                it.assertCountEquals(2)
                it[0].onChild().assertTextEquals("n1")
                it[1].onChild().assertTextEquals("n2")
            }
    }

    @Test
    fun saveNoteOnButtonClick() = runComposeUiTest {
        val state = mutableStateOf(NotesState(listOf("n1")))
        val inputState = mutableStateOf("" to false)
        var textToUpdate: String? = null
        var saveNoteTriggered = false
        setContent {
            NotesScreen(
                state.value,
                inputState,
                { textToUpdate = it },
                { saveNoteTriggered = true })
        }

        onNodeWithTag(NotesScreenTags.NOTE_INPUT).let {
            it.performTextInput("n2")
            assertEquals("n2", textToUpdate)
            it.assertIsFocused()
        }
        inputState.value = "n2" to true
        onNodeWithTag(NotesScreenTags.DONE_BUTTON)
            .assertIsEnabled()
            .performClick()
        assertTrue(saveNoteTriggered)
        onNodeWithTag(NotesScreenTags.NOTE_INPUT)
            .assertIsNotFocused()
    }

    @Test
    fun saveNoteOnImeDone() = runComposeUiTest {
        val state = mutableStateOf(NotesState(emptyList()))
        val inputState = mutableStateOf("" to false)
        var textToUpdate: String? = null
        var saveNoteTriggered = false
        setContent {
            NotesScreen(
                state.value,
                inputState,
                { textToUpdate = it },
                { saveNoteTriggered = true })
        }

        onNodeWithTag(NotesScreenTags.NOTE_INPUT).let {
            it.performTextInput("n3")
            assertEquals("n3", textToUpdate)
            it.performImeAction()
            assertTrue(saveNoteTriggered)
        }
    }

}