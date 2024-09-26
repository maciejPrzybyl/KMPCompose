package org.macpry.kmpcompose

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            Scaffold(
                topBar = {
                    TopBar(
                        backStackEntry?.destination?.label?.toString().orEmpty(),
                        navController.previousBackStackEntry != null,
                        { navController.navigateUp() }
                    )
                },
                bottomBar = {}
            ) {
                Navigation(navController, mainState)
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
private fun Navigation(
    navController: NavHostController,
    mainState: MainState
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main
    ) {
        //TODO Get context and set label from resources
        composableWithLabel<Screen.Main>("Main") {
            MainScreen(
                state = mainState,
                onOpenDetails = {
                    navController.navigate(Screen.Details(it))
                }
            )
        }
        composableWithLabel<Screen.Details>("Details") {
            val detailsViewModel: DetailsViewModel = koinViewModel()
            val detailsState by detailsViewModel.state.collectAsStateWithLifecycle()
            it.toRoute<Screen.Details>().arg.let {
                detailsViewModel.setArg(it)
            }
            DetailsScreen(
                state = detailsState
            )
        }
    }
}
