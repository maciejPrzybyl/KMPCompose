package org.macpry.kmpcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.tooling.preview.Preview
import org.macpry.kmpcompose.data.network.ImageResponse
import org.macpry.kmpcompose.screens.main.ImagesState
import org.macpry.kmpcompose.screens.main.MainScreen
import org.macpry.kmpcompose.screens.main.MainState
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(MainState("12:12", ImagesState.Success(listOf(ImageResponse(1, "url", "aaaaaaaa"))), 0), {}, {}, null, null)
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    NotesScreen(NotesState(listOf("sdfh", "opapad")), rememberUpdatedState("" to false), {}, {})
}
