@file:OptIn(ExperimentalSharedTransitionApi::class)

package org.macpry.kmpcompose

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
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
import androidx.core.bundle.Bundle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kmpcompose.composeapp.generated.resources.Res
import kmpcompose.composeapp.generated.resources.app_dialog_title
import kmpcompose.composeapp.generated.resources.ok
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.macpry.kmpcompose.screens.AppNavigationRoutes
import org.macpry.kmpcompose.screens.HomeBottomNavigation
import org.macpry.kmpcompose.screens.imagedetail.ImageDetailScreen
import org.macpry.kmpcompose.screens.main.MainScreen
import org.macpry.kmpcompose.screens.main.MainViewModel
import org.macpry.kmpcompose.screens.maps.MapsScreen
import org.macpry.kmpcompose.screens.maps.MapsViewModel
import org.macpry.kmpcompose.screens.maps.model.Coordinates
import org.macpry.kmpcompose.screens.notes.NotesScreen
import org.macpry.kmpcompose.screens.notes.NotesViewModel
import org.macpry.kmpcompose.screens.settings.SettingsScreen
import org.macpry.kmpcompose.screens.settings.SettingsViewModel
import kotlin.reflect.typeOf

@Composable
@Preview
fun App() {
    KoinContext {
        MaterialTheme {
            AppNavigation()
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavigation() {
    val appNavController = rememberNavController()
    SharedTransitionLayout {
        NavHost(
            navController = appNavController,
            startDestination = AppNavigationRoutes.Home
        ) {
            composable<AppNavigationRoutes.Home> {
                HomeNavigation(
                    onOpenMaps = { appNavController.navigate(AppNavigationRoutes.Maps(it)) },
                    onOpenImageDetails = {
                        appNavController.navigate(
                            AppNavigationRoutes.ImageDetail(
                                it
                            )
                        )
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }
            composable<AppNavigationRoutes.Maps>(
                typeMap = mapOf(typeOf<Coordinates>() to CoordinatesNavType),
            ) {
                it.toRoute<AppNavigationRoutes.Maps>().coordinates.let { coordinates ->
                    val mapsViewModel: MapsViewModel = koinViewModel()
                    mapsViewModel.setLocation(coordinates)
                    val mapsState by mapsViewModel.mapsState.collectAsStateWithLifecycle()
                    MapsScreen(
                        mapsState,
                        { appNavController.navigateUp() },
                        { mapsViewModel.onLocationPermissionsChanged(it) })
                }
            }
            composable<AppNavigationRoutes.ImageDetail> {
                it.toRoute<AppNavigationRoutes.ImageDetail>().url.let { url ->
                    ImageDetailScreen(
                        url = url,
                        onBack = { appNavController.navigateUp() },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable
                    )
                }
            }
        }
    }
}

val CoordinatesNavType = object : NavType<Coordinates>(
    isNullableAllowed = false,
) {
    override fun get(bundle: Bundle, key: String): Coordinates? {
        return Json.decodeFromString(bundle.getString(key) ?: return null)
    }

    override fun parseValue(value: String): Coordinates {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Coordinates) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun serializeAsValue(value: Coordinates): String {
        return Json.encodeToString(value)
    }
}

@Composable
private fun HomeNavigation(
    mainViewModel: MainViewModel = koinViewModel(),
    onOpenMaps: (Coordinates) -> Unit,
    onOpenImageDetails: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
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
                { onOpenMaps(Coordinates(it.first, it.second)) },
                onOpenImageDetails,
                sharedTransitionScope,
                animatedVisibilityScope
            )

            HomeBottomNavigation.Notes -> {
                val notesViewModel: NotesViewModel = koinViewModel()
                val notesState by notesViewModel.notesState.collectAsStateWithLifecycle()
                NotesScreen(
                    notesState,
                    notesViewModel.inputState,
                    notesViewModel::updateInput,
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
                Text(text = stringResource(Res.string.app_dialog_title))
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
                    Text(text = stringResource(Res.string.ok))
                }
            },
        )
    }
}
