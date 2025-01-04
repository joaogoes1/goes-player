package com.goesplayer.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.goesplayer.AppTheme

@Composable
fun GoesPlayerApp(
    activityViewModel: MainActivityViewModel,
) {
    AppTheme {
        GoesPlayerNavGraph(
            activityViewModel = activityViewModel,
        )
    }
}