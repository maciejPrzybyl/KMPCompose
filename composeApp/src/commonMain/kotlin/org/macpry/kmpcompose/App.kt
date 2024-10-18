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
import kmpcompose.composeapp.generated.resources.Res
import kmpcompose.composeapp.generated.resources.title_dialog
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.macpry.kmpcompose.screens.BottomNavigation
import org.macpry.kmpcompose.screens.MainViewModel
import org.macpry.kmpcompose.screens.args.ArgsFromNavigationScreen
import org.macpry.kmpcompose.screens.args.ArgsFromViewModelScreen
import org.macpry.kmpcompose.screens.main.MainScreen
import org.macpry.kmpcompose.screens.notes.NotesScreen
import org.macpry.kmpcompose.screens.notes.NotesViewModel

@Composable
@Preview
fun App(mainViewModel: MainViewModel = koinViewModel()) {
    KoinContext {
        MaterialTheme {
            var isDialogVisible by remember { mutableStateOf(false) }
            //TODO Fix saving mainState when app is recreated
            val mainState by mainViewModel.state.collectAsStateWithLifecycle()
            var currentDestination by rememberSaveable { mutableStateOf(BottomNavigation.Main) }
            var navArgsInputText by remember { mutableStateOf("") }
            val navArgsOnTextChanged: (String) -> Unit = { navArgsInputText = it }
            val navArgsOnOpenDetails: () -> Unit = {
                //navController.navigateTo(Route.DetailsNavArgs(navArgsInputText))
            }
            NavigationSuiteScaffold(
                navigationSuiteItems = {
                    listOf(
                        BottomNavigation.Main,
                        BottomNavigation.DetailsNavArgs,
                        BottomNavigation.DetailsCommonState,
                        BottomNavigation.Notes
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
                MainAlertDialog(
                    isDialogVisible,
                    { isDialogVisible = false }
                )
                when (currentDestination) {
                    BottomNavigation.Main -> MainScreen(
                        state = mainState,
                        mainViewModel.images,
                        navArgsInputText = navArgsInputText,
                        navArgsOnTextChanged = navArgsOnTextChanged,
                        navArgsOnOpenDetails = navArgsOnOpenDetails,
                        commonOnTextChanged = mainViewModel::updateInput,
                        commonOnOpenDetails = {
                            currentDestination = BottomNavigation.DetailsCommonState
                        }
                    )

                    BottomNavigation.DetailsNavArgs -> /*it.toRoute<Route.DetailsNavArgs>().arg.let {
                        ArgsFromNavigationScreen(it)
                    }*/
                        ArgsFromNavigationScreen("PASS ARGS!!!")

                    BottomNavigation.DetailsCommonState -> ArgsFromViewModelScreen(mainState.inputText)
                    BottomNavigation.Notes -> {
                        val notesViewModel: NotesViewModel = koinViewModel()
                        val notesState by notesViewModel.notesState.collectAsStateWithLifecycle()
                        NotesScreen(
                            notesState,
                            notesViewModel::saveNote
                        )
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
    }
}

@Composable
fun MainAlertDialog(
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
