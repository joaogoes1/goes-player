package com.goesplayer.presentation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object GoesPlayerDestinations {
    const val PLAYER_ROUTE = "player"
    const val HOME_ROUTE = "home"
}

class GoesPlayerNavigationActions(navController: NavHostController) {
    val navigateToPlayer: () -> Unit = {
        navController.navigate(GoesPlayerDestinations.PLAYER_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
