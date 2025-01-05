package com.goesplayer.presentation.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.goesplayer.presentation.MainActivityViewModel
import com.goesplayer.presentation.PlayerViewState

@Composable
fun PlayerRoute(
    activityViewModel: MainActivityViewModel,
    navController: NavController,
) {
    PlayerScreen(
        repeatAction = activityViewModel::changeRepeatState,
        skipPreviousAction = activityViewModel::skipToPrevious,
        playPauseAction = activityViewModel::playOrPause,
        skipNextAction = activityViewModel::skipToNext,
        shuffleAction = activityViewModel::changeShuffleState,
        getPositionAction = activityViewModel::getPosition,
        changeProgressAction = activityViewModel::changeProgress,
        progress = activityViewModel.playerProgress.observeAsState(0L),
        playerViewState = activityViewModel.playerViewState.observeAsState(PlayerViewState.Loading),
        navController = navController,
    )
}
