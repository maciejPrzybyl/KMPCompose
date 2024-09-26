package org.macpry.kmpcompose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.macpry.kmpcompose.di.koinConfiguration
import org.macpry.kmpcompose.screens.MainViewModel
import org.macpry.kmpcompose.screens.Route
import org.macpry.kmpcompose.screens.Screen
import org.macpry.kmpcompose.screens.details.DetailsCommonViewModelScreen
import org.macpry.kmpcompose.screens.details.DetailsNavArgsScreen
import org.macpry.kmpcompose.screens.details.DetailsViewModel
import org.macpry.kmpcompose.screens.main.MainScreen
import org.macpry.kmpcompose.utils.composableWithLabel

@Composable
@Preview
fun App() {
    KoinApplication(koinConfiguration()) {
        MaterialTheme {
            val navController = rememberNavController()

            val backStackEntry by navController.currentBackStackEntryAsState()
            var navArgsInputText by remember { mutableStateOf("") }
            val navArgsOnTextChanged: (String) -> Unit = { navArgsInputText = it }
            val navArgsOnOpenDetails: () -> Unit =
                { navController.navigate(Route.DetailsNavArgs(navArgsInputText)) }
            Scaffold(
                topBar = {
                    TopBar(
                        title = backStackEntry?.destination?.label?.toString().orEmpty(),
                        canNavigateBack = navController.previousBackStackEntry != null,
                        onNavigateBack = { navController.navigateUp() }
                    )
                },
                bottomBar = {
                    BottomBar(
                        navController,
                        navArgsOnOpenDetails
                    )
                }
            ) {
                Navigation(
                    navController = navController,
                    navArgsInputText = navArgsInputText,
                    navArgsOnTextChanged = navArgsOnTextChanged,
                    navArgsOnOpenDetails = navArgsOnOpenDetails
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    //TODO Use resource
    title: String,//Resource,
    canNavigateBack: Boolean,
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = { Text(/*stringResource*/(title)) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    onClick = { onNavigateBack() },
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            }
        }
    )
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    navArgsOnOpenDetails: () -> Unit
) {
    // TODO No reflection available (so far) for below:
    // val routes = Screen::class.sealedSubclasses.forEach {}
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        listOf(Screen.Main, Screen.DetailsNavArgs, Screen.DetailsCommonState).forEach { screen ->
            NavigationBarItem(
                selected = backStackEntry?.destination?.hierarchy?.any { it.hasRoute(screen.route) } == true,
                onClick = {
                    if (screen.route == Screen.DetailsNavArgs.route) {
                        navArgsOnOpenDetails()
                    } else {
                        navController.navigate(screen.route)
                    }
                },
                icon = { Icon(screen.icon, screen.label) },
                label = { Text(screen.label) }
            )
        }
    }
}

@Composable
private fun Navigation(
    navController: NavHostController,
    navArgsInputText: String,
    navArgsOnTextChanged: (String) -> Unit,
    navArgsOnOpenDetails: () -> Unit
) {
    val mainViewModel: MainViewModel = koinViewModel()
    val mainState by mainViewModel.state.collectAsStateWithLifecycle()
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        //TODO Get context and set label from resources
        // TODO No reflection available, so far for below:
        // val routes = Screen::class.sealedSubclasses.forEach {}
        composableWithLabel<Route.Main>(Screen.Main.label) {
            MainScreen(
                state = mainState,
                navArgsInputText = navArgsInputText,
                navArgsOnTextChanged = navArgsOnTextChanged,
                navArgsOnOpenDetails = navArgsOnOpenDetails,
                commonOnTextChanged = { mainViewModel.updateInput(it) },
                commonOnOpenDetails = { navController.navigate(Screen.DetailsCommonState.route) }
            )
        }
        composableWithLabel<Route.DetailsNavArgs>(Screen.DetailsNavArgs.label) {
            it.toRoute<Route.DetailsNavArgs>().arg.let {
                DetailsNavArgsScreen(it)
            }
        }
        composableWithLabel<Route.DetailsCommonState>(Screen.DetailsCommonState.label) {
            val detailsViewModel: DetailsViewModel = koinViewModel()
            val detailsState by detailsViewModel.state.collectAsStateWithLifecycle()
            DetailsCommonViewModelScreen(mainState.inputText)
        }
    }
}
