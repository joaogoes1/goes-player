package com.goesplayer.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.goesplayer.R

@Composable
fun PlayPauseButtonIcon(
    isPlaying: Boolean,
    tint: Color = Color.Black,
) {
    if (isPlaying)
        Icon(
            Icons.Filled.Pause,
            contentDescription = stringResource(R.string.pause_button_content_description),
            tint = tint
        )
    else
        Icon(
            Icons.Filled.PlayArrow,
            contentDescription = stringResource(R.string.play_button_content_description),
            tint = tint
        )
}