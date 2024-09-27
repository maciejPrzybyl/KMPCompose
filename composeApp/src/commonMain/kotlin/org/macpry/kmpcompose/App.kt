package org.macpry.kmpcompose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
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
import org.macpry.kmpcompose.screens.BottomNavigation
import org.macpry.kmpcompose.screens.MainViewModel
import org.macpry.kmpcompose.screens.Route
import org.macpry.kmpcompose.screens.details.DetailsCommonViewModelScreen
import org.macpry.kmpcompose.screens.details.DetailsNavArgsScreen
import org.macpry.kmpcompose.screens.details.DetailsViewModel
import org.macpry.kmpcompose.screens.findRoute
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
            val navArgsOnOpenDetails: () -> Unit = {
                navController.navigateTo(Route.DetailsNavArgs(navArgsInputText))
            }
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
            ) { innerPadding ->
                Navigation(
                    innerPadding,
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
        listOf(
            BottomNavigation.Main,
            BottomNavigation.DetailsNavArgs,
            BottomNavigation.DetailsCommonState
        ).forEach { screen ->
            NavigationBarItem(
                selected = backStackEntry?.destination?.hierarchy?.any { it.hasRoute(screen.findRoute()) } == true,
                onClick = {
                    when (screen) {
                        BottomNavigation.Main -> navController.navigateTo(Route.Main)
                        BottomNavigation.DetailsNavArgs -> navArgsOnOpenDetails()
                        BottomNavigation.DetailsCommonState -> navController.navigateTo(Route.DetailsCommonState)
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
    padding: PaddingValues,
    navController: NavHostController,
    navArgsInputText: String,
    navArgsOnTextChanged: (String) -> Unit,
    navArgsOnOpenDetails: () -> Unit
) {
    val mainViewModel: MainViewModel = koinViewModel()
    val mainState by mainViewModel.state.collectAsStateWithLifecycle()
    NavHost(
        navController = navController,
        startDestination = Route.Main,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(padding)
    ) {
        //TODO Get context and set label from resources
        // TODO No reflection available, so far for below:
        // val routes = Screen::class.sealedSubclasses.forEach {}
        composableWithLabel<Route.Main>(BottomNavigation.Main.label) {
            MainScreen(
                state = mainState,
                mainViewModel.images,
                navArgsInputText = navArgsInputText,
                navArgsOnTextChanged = navArgsOnTextChanged,
                navArgsOnOpenDetails = navArgsOnOpenDetails,
                commonOnTextChanged = { mainViewModel.updateInput(it) },
                commonOnOpenDetails = { navController.navigateTo(Route.DetailsCommonState) }
            )
        }
        composableWithLabel<Route.DetailsNavArgs>(BottomNavigation.DetailsNavArgs.label) {
            it.toRoute<Route.DetailsNavArgs>().arg.let {
                DetailsNavArgsScreen(it)
            }
        }
        composableWithLabel<Route.DetailsCommonState>(BottomNavigation.DetailsCommonState.label) {
            val detailsViewModel: DetailsViewModel = koinViewModel()
            val detailsState by detailsViewModel.state.collectAsStateWithLifecycle()
            DetailsCommonViewModelScreen(mainState.inputText)
        }
    }
}

private fun NavController.navigateTo(route: Route) = navigate(route) {
    graph.startDestinationRoute?.let {
        popUpTo<Route.Main> {
            saveState = true
        }
    }
    launchSingleTop = true
    restoreState = true
}
