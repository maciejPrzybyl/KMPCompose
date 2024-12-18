package org.macpry.kmpcompose.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    state: SettingsState,
    saveSetting: (Int) -> Unit
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
            .testTag(SettingsScreenTags.ITEMS_CONTAINER),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(state.settings) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { saveSetting(it.first) }
                    .background(Color.LightGray)
                    .testTag(SettingsScreenTags.ITEM),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = it.second,
                    onClick = null
                )
                Text(it.first.toString())
            }
        }
    }
}

object SettingsScreenTags {
    const val ITEMS_CONTAINER = "ITEMS_CONTAINER"
    const val ITEM = "ITEM"
}
