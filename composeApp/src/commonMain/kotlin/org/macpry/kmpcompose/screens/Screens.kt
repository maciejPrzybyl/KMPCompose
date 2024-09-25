package org.macpry.kmpcompose.screens

import kmpcompose.composeapp.generated.resources.Res
import kmpcompose.composeapp.generated.resources.title_details
import kmpcompose.composeapp.generated.resources.title_main
import org.jetbrains.compose.resources.StringResource

enum class Screen(val title: StringResource, val route: String) {
    Main(Res.string.title_main, "Main"),
    Details(Res.string.title_details, "Details",)
}