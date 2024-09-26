package org.macpry.kmpcompose

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import org.macpry.kmpcompose.screens.MainState
import org.macpry.kmpcompose.screens.MainViewModel
import org.macpry.kmpcompose.screens.Route
import org.macpry.kmpcompose.screens.Screen
import org.macpry.kmpcompose.screens.details.DetailsScreen
import org.macpry.kmpcompose.screens.details.DetailsViewModel
import org.macpry.kmpcompose.screens.main.MainScreen
import org.macpry.kmpcompose.utils.composableWithLabel

@Composable
@Preview
fun App() {
    KoinApplication(koinConfiguration()) {
        MaterialTheme {
            val navController = rememberNavController()

            val mainViewModel: MainViewModel = koinViewModel()
            val mainState by mainViewModel.state.collectAsStateWithLifecycle()

            val backStackEntry by navController.currentBackStackEntryAsState()
            var inputText by remember { mutableStateOf("") }
            val onTextChanged: (String) -> Unit = { inputText = it }
            val onOpenDetails: () -> Unit = { navController.navigate(Route.Details(inputText)) }
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
                        onOpenDetails
                    )
                }
            ) {
                Navigation(
                    navController = navController,
                    mainState = mainState,
                    inputText = inputText,
                    onTextChanged = onTextChanged,
                    onOpenDetails = onOpenDetails
                )
            }
        }
    }
}

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
    onOpenDetails: () -> Unit
) {
    // TODO No reflection available (so far) for below:
    // val routes = Screen::class.sealedSubclasses.forEach {}
    BottomNavigation {
        val backStackEntry by navController.currentBackStackEntryAsState()
        listOf(Screen.Main, Screen.Details).forEach { screen ->
            BottomNavigationItem(
                selected = backStackEntry?.destination?.hierarchy?.any { it.hasRoute(screen.route) } == true,
                onClick = onOpenDetails,
                icon = { Icon(screen.icon, screen.label) },
                label = { Text(screen.label) }
            )
        }
    }
}

@Composable
private fun Navigation(
    navController: NavHostController,
    mainState: MainState,
    inputText: String,
    onTextChanged: (String) -> Unit,
    onOpenDetails: () -> Unit
) {
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
                inputText = inputText,
                onTextChanged = onTextChanged,
                onOpenDetails = onOpenDetails
            )
        }
        composableWithLabel<Route.Details>(Screen.Details.label) {
            val detailsViewModel: DetailsViewModel = koinViewModel()
            val detailsState by detailsViewModel.state.collectAsStateWithLifecycle()
            it.toRoute<Route.Details>().arg.let {
                detailsViewModel.setArg(it)
            }
            DetailsScreen(
                state = detailsState
            )
        }
    }
}
