package org.macpry.kmpcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.tooling.preview.Preview
import org.macpry.kmpcompose.screens.notes.NotesScreen
import org.macpry.kmpcompose.screens.notes.NotesState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppAndroidPreview() {
    App()
}


@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    NotesScreen(NotesState(listOf("sdfh", "opapad")), rememberUpdatedState("" to false), {}, {})
}