package com.goesplayer.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.goesplayer.presentation.home.HomeRoute
import com.goesplayer.presentation.home.HomeViewModel
import com.goesplayer.presentation.player.PlayerRoute

@Composable
fun GoesPlayerNavGraph(
    activityViewModel: MainActivityViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = GoesPlayerDestinations.HOME_ROUTE,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(
            route = GoesPlayerDestinations.HOME_ROUTE,
        ) {
            val homeViewModel: HomeViewModel = viewModel()
            HomeRoute(
                { navController.navigate(GoesPlayerDestinations.PLAYER_ROUTE) },
                activityViewModel = activityViewModel,
                homeViewModel = homeViewModel,
            )
        }
        composable(
            route = GoesPlayerDestinations.PLAYER_ROUTE
        ) {
            PlayerRoute(
                activityViewModel = activityViewModel
            )
        }
    }
}