package org.macpry.kmpcompose

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.macpry.kmpcompose.di.koinConfiguration
import org.macpry.kmpcompose.screens.MainViewModel
import org.macpry.kmpcompose.screens.Screen
import org.macpry.kmpcompose.screens.details.DetailsScreen
import org.macpry.kmpcompose.screens.details.DetailsViewModel
import org.macpry.kmpcompose.screens.main.MainScreen

@Composable
@Preview
fun App() {
    KoinApplication( koinConfiguration() ) {
        MaterialTheme {
            val navController = rememberNavController()
            val mainViewModel: MainViewModel = koinViewModel()
            val mainState by mainViewModel.state.collectAsStateWithLifecycle()
            NavHost(
                navController = navController,
                startDestination = Screen.Main.route
            ) {
                composable(Screen.Main.route) {
                    MainScreen(
                        state = mainState,
                        onOpenDetails = {
                            navController.currentBackStackEntry?.savedStateHandle?.set("ARG_KEY", it)
                            navController.navigate(Screen.Details.route)
                        }
                    )
                }
                composable(Screen.Details.route) {
                    val detailsViewModel: DetailsViewModel = koinViewModel()
                    val detailsState by detailsViewModel.state.collectAsStateWithLifecycle()
                    navController.previousBackStackEntry?.savedStateHandle?.get<String>("ARG_KEY").let {
                        detailsViewModel.setArg(it)
                    }
                    DetailsScreen(
                        state = detailsState,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
