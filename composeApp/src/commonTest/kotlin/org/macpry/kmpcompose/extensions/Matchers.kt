package org.macpry.kmpcompose.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsMatcher
import org.macpry.kmpcompose.ui.ColorId

fun hasColor(expectedColor: Color) =
    SemanticsMatcher.expectValue(ColorId, expectedColor)
