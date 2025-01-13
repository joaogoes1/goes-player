package com.goesplayer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.goesplayer.R
import com.goesplayer.presentation.PlayerViewState


@Composable
fun MiniPlayer(
    skipToPreviousAction: () -> Unit,
    playOrPauseAction: () -> Unit,
    skipToNextAction: () -> Unit,
    navigateToPlayer: () -> Unit,
    playerViewState: State<PlayerViewState>,
) {
    val currentState = playerViewState.value
    if (currentState is PlayerViewState.Success) {
        Box(
            modifier = Modifier
                .testTag("mini_player")
                .clip(shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp))
                .fillMaxWidth()
                .background(Color(0xFF101010))
                .clickable {
                    navigateToPlayer() // TODO: Add transition bottom to top
                }
        ) {
            Row {
                AlbumImage(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 2.dp, start = 4.dp, end = 0.dp)
                        .size(48.dp)
                        .clip(shape = RoundedCornerShape(12.dp)),
                    albumUri = currentState.album,
                )
                Spacer(
                    Modifier
                        .width(8.dp)
                        .align(Alignment.CenterVertically)
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                ) {
                    Text(
                        currentState.songName,
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Text(
                        currentState.artist,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                IconButton(
                    onClick = skipToPreviousAction,
                    modifier = Modifier
                        .height(30.dp)
                        .align(Alignment.CenterVertically),
                ) {
                    Icon(
                        Icons.Filled.SkipPrevious,
                        contentDescription = stringResource(
                            R.string.skip_previous_button_content_description
                        ),
                        tint = Color.White,
                    )
                }
                IconButton(
                    onClick = playOrPauseAction,
                    modifier = Modifier
                        .height(30.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    PlayPauseButtonIcon(
                        currentState.isPlaying,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                IconButton(
                    onClick = skipToNextAction,
                    modifier = Modifier
                        .height(30.dp)
                        .align(Alignment.CenterVertically),
                ) {
                    Icon(
                        Icons.Filled.SkipNext,
                        contentDescription = stringResource(
                            R.string.skip_next_button_content_description
                        ),
                        tint = Color.White,
                    )
                }
            }
        }
    }
}
