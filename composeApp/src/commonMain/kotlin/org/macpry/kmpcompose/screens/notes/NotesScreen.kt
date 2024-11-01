package org.macpry.kmpcompose.screens.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kmpcompose.composeapp.generated.resources.Res
import kmpcompose.composeapp.generated.resources.save
import kmpcompose.composeapp.generated.resources.save_something
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotesScreen(
    state: NotesState,
    inputState: State<Pair<String, Boolean>>,
    updateInput: (String) -> Unit,
    saveText: () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        Arrangement.SpaceEvenly,
        Alignment.CenterHorizontally
    ) {
        val focusManager = LocalFocusManager.current

        fun onDone() {
            saveText()
            focusManager.clearFocus()
        }
        TextField(
            value = inputState.value.first,
            onValueChange = { updateInput(it) },
            modifier = Modifier.testTag(NotesScreenTags.NOTE_INPUT),
            label = { Text(stringResource(Res.string.save_something)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onDone()
                }
            ),
            singleLine = true
        )
        Button(
            onClick = { onDone() },
            modifier = Modifier.testTag(NotesScreenTags.DONE_BUTTON),
            enabled = inputState.value.second
        ) {
            Text(stringResource(Res.string.save))
        }

        LazyColumn(
            modifier = Modifier.testTag(NotesScreenTags.NOTES_LIST),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(state.notes) {
                ElevatedCard {
                    Text(
                        text = it,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }
    }
}

object NotesScreenTags {
    const val NOTE_INPUT = "NOTE_INPUT"
    const val DONE_BUTTON = "DONE_BUTTON"
    const val NOTES_LIST = "NOTES_LIST"
}
