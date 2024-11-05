package org.macpry.kmpcompose.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

val ColorId = SemanticsPropertyKey<Color>("ColorId")
var SemanticsPropertyReceiver.colorId by ColorId
