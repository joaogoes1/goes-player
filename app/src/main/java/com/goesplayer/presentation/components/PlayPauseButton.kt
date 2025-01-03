package com.goesplayer.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.goesplayer.R

@Composable
fun PlayPauseButtonIcon(
    isPlaying: Boolean
) {
    if (isPlaying)
        Icon(Icons.Filled.Pause, contentDescription = stringResource(R.string.pause_button_content_description))
    else
        Icon(Icons.Filled.PlayArrow, contentDescription = stringResource(R.string.play_button_content_description))
}