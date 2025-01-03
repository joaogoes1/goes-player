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
import com.goesplayer.presentation.musiclist.SearchProperties
import com.goesplayer.presentation.player.PlayerRoute
import kotlinx.serialization.Serializable

@Serializable
data class MusicList(
    val title: String,
    val searchProperties: SearchProperties,
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
                { navController.navigate(GoesPlayerDestinations.PLAYER_ROUTE) },
                navigateToMusicList = { title, searchProperties ->
                    navController.navigate(
                        MusicList(
                            title,
                            searchProperties,
                        )
                    )
                },
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
        composable<MusicList> { backStackEntry ->
            val route = backStackEntry.toRoute<MusicList>()
            MusicListRoute(
                title = route.title,
                musicList = activityViewModel.songList.value ?: emptyList(),
                searchProperties = route.searchProperties,
            )
        }
    }
}