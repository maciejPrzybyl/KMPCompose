package org.macpry.kmpcompose.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .testTag(SettingsScreenTags.ITEMS_CONTAINER),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(state.settings) { index, item ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { saveSetting(item.first) }
                    .padding(vertical = 10.dp)
                    .testTag(SettingsScreenTags.ITEM),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = item.second,
                    onClick = null
                )
                Text(item.first.toString())
            }
            if (index < state.settings.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

object SettingsScreenTags {
    const val ITEMS_CONTAINER = "ITEMS_CONTAINER"
    const val ITEM = "ITEM"
}
