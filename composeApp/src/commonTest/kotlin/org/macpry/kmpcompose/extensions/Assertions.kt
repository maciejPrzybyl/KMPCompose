package org.macpry.kmpcompose.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert

fun SemanticsNodeInteraction.assertColorEquals(expectedColor: Color): SemanticsNodeInteraction =
    assert(hasColor(expectedColor))
