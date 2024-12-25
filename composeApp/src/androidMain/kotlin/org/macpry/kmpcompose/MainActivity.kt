package org.macpry.kmpcompose

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import org.macpry.kmpcompose.data.network.ImageResponse
import org.macpry.kmpcompose.screens.main.ImagesState
import org.macpry.kmpcompose.screens.main.MainScreen
import org.macpry.kmpcompose.screens.main.MainState
import org.macpry.kmpcompose.screens.notes.NotesScreen
import org.macpry.kmpcompose.screens.notes.NotesState
import org.macpry.kmpcompose.theme.darkScheme
import org.macpry.kmpcompose.theme.lightScheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            val darkTheme = isSystemInDarkTheme()
            val colorScheme = when {
                dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
                dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
                darkTheme -> darkScheme
                else -> lightScheme
            }
            App(colorScheme)
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        MainState("12:12", ImagesState.Success(listOf(ImageResponse(1, "url", "aaaaaaaa")))),
        {},
        {},
        null,
        null
    )
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    NotesScreen(NotesState(listOf("sdfh", "opapad")), rememberUpdatedState("" to false), {}, {})
}
