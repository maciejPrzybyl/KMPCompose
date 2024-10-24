package org.macpry.kmpcompose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kmpcompose.composeapp.generated.resources.Res
import kmpcompose.composeapp.generated.resources.title_dialog
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.macpry.kmpcompose.screens.AppNavigationRoutes
import org.macpry.kmpcompose.screens.HomeBottomNavigation
import org.macpry.kmpcompose.screens.MainViewModel
import org.macpry.kmpcompose.screens.main.MainScreen
import org.macpry.kmpcompose.screens.maps.MapsScreen
import org.macpry.kmpcompose.screens.notes.NotesScreen
import org.macpry.kmpcompose.screens.notes.NotesViewModel
import org.macpry.kmpcompose.screens.settings.SettingsScreen
import org.macpry.kmpcompose.screens.settings.SettingsViewModel

@Composable
@Preview
fun App() {
    KoinContext {
        MaterialTheme {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val appNavController = rememberNavController()
    NavHost(
        navController = appNavController,
        startDestination = AppNavigationRoutes.Home
    ) {
        composable<AppNavigationRoutes.Home> {
            HomeNavigation {
                appNavController.navigate(AppNavigationRoutes.Maps(it))
            }
        }
        composable<AppNavigationRoutes.Maps> {
            it.toRoute<AppNavigationRoutes.Maps>().mapsDestination.let {
                MapsScreen(it)
            }
        }
    }
}

@Composable
private fun HomeNavigation(
    mainViewModel: MainViewModel = koinViewModel(),
    onMapsDestinationPicked: (String?) -> Unit
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    //TODO Fix saving mainState when app is recreated
    val mainState by mainViewModel.state.collectAsStateWithLifecycle()
    var currentDestination by rememberSaveable { mutableStateOf(HomeBottomNavigation.Main) }
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            listOf(
                HomeBottomNavigation.Main,
                HomeBottomNavigation.Notes,
                HomeBottomNavigation.Settings
            ).forEach { screen ->
                item(
                    selected = screen == currentDestination,
                    onClick = { currentDestination = screen },
                    icon = { Icon(screen.icon, stringResource(screen.label)) },
                    label = { Text(stringResource(screen.label)) }
                )
            }
        }
    ) {
        AppAlertDialog(
            isDialogVisible,
            { isDialogVisible = false }
        )
        when (currentDestination) {
            HomeBottomNavigation.Main -> MainScreen(
                state = mainState,
                images = mainViewModel.images,
                onDestinationInputTextChanged = mainViewModel::updateInput,
                onDestinationPicked = {
                    onMapsDestinationPicked(mainState.destination)
                }
            )

            HomeBottomNavigation.Notes -> {
                val notesViewModel: NotesViewModel = koinViewModel()
                val notesState by notesViewModel.notesState.collectAsStateWithLifecycle()
                NotesScreen(
                    notesState,
                    notesViewModel::saveNote
                )
            }

            HomeBottomNavigation.Settings -> {
                val settingsViewModel: SettingsViewModel = koinViewModel()
                val settingsState by settingsViewModel.settingsState.collectAsStateWithLifecycle()
                SettingsScreen(settingsState, settingsViewModel::saveSetting)
            }
        }
        Box(Modifier.fillMaxSize()) {
            FloatingActionButton(
                onClick = {
                    isDialogVisible = true
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }
}

@Composable
fun AppAlertDialog(
    isVisible: Boolean,
    onDismissRequest: () -> Unit
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            modifier = Modifier.padding(vertical = 100.dp),
            title = {
                Text(text = stringResource(Res.string.title_dialog))
            },
            text = {
                LazyColumn {
                    (1..30).forEach {
                        item {
                            Text(
                                "$it",
                                Modifier.padding(20.dp).fillMaxWidth()
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onDismissRequest,
                ) {
                    Text(text = "Ok")
                }
            },
        )
    }
}
