import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsMatcher

fun hasColor(expectedColor: Color) =
    SemanticsMatcher.expectValue(ColorId, expectedColor)
