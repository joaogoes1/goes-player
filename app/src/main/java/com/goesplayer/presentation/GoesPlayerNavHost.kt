package com.goesplayer.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.goesplayer.presentation.home.HomeRoute
import com.goesplayer.presentation.home.HomeViewModel
import com.goesplayer.presentation.musiclist.MusicListRoute
import com.goesplayer.presentation.player.PlayerRoute
import kotlinx.serialization.Serializable

@Serializable
data class MusicListRouteConfig(
    val pageTitle: String = "",
    val artist: String? = null,
    val album: String? = null,
    val genre: String? = null,
    val folder: String? = null,
)

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
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeRoute(
                navigateToPlayer = { navController.navigate(GoesPlayerDestinations.PLAYER_ROUTE) },
                navigateToMusicList = { navController.navigate(it) },
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
        composable<MusicListRouteConfig> { backStackEntry ->
            val params = backStackEntry.toRoute<MusicListRouteConfig>()
            MusicListRoute(
                title = params.pageTitle,
                musicList = activityViewModel.songList.value ?: emptyList(),
                searchProperties = params,
            )
        }
    }
}