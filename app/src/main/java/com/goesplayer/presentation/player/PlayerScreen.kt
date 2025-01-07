package com.goesplayer.presentation.player

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.navigation.NavController
import com.goesplayer.R
import com.goesplayer.presentation.PlayerViewState
import com.goesplayer.presentation.components.BackButton
import com.goesplayer.presentation.components.ErrorScreen
import com.goesplayer.presentation.components.LoadingScreen
import com.goesplayer.presentation.components.PlayPauseButtonIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    repeatAction: () -> Unit,
    skipPreviousAction: () -> Unit,
    playPauseAction: () -> Unit,
    skipNextAction: () -> Unit,
    shuffleAction: () -> Unit,
    getPositionAction: () -> Long,
    changeProgressAction: (Long) -> Unit,
    progress: State<Long>,
    playerViewState: State<PlayerViewState>,
    navController: NavController,
) {
    when (val currentState = playerViewState.value) {
        is PlayerViewState.None, PlayerViewState.Error -> { // T
            ErrorScreen { }
        }

        is PlayerViewState.Loading -> {
            LoadingScreen()
        }

        is PlayerViewState.Success -> {
            Scaffold(topBar = {
                TopAppBar(
                    navigationIcon = { BackButton(navController) },
                    title = { Text(stringResource(R.string.app_name)) },
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                )
            }) { _ ->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        Modifier
                            .aspectRatio(1f)
                            .align(Alignment.CenterHorizontally)
                            .weight(1f)
                            .padding(horizontal = 24.dp)
                    ) {
                        val albumArt = currentState.album?.retrieveImage(LocalContext.current)
                        if (albumArt != null) {
                            Image(
                                bitmap = albumArt.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .aspectRatio(1f)
                            )
                        } else {
                            Image(
                                painter = painterResource(R.mipmap.teste_album),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .aspectRatio(1f)
                            )
                        }
                    }
                    Text(
                        text = currentState.songName,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        text = currentState.artist,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.titleSmall,
                    )
                    PlayerSlider(
                        changeProgressAction = changeProgressAction,
                        getPositionAction = getPositionAction,
                        initialValue = progress.value,
                        durationSeconds = currentState.durationInMs,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        IconButton(
                            onClick = shuffleAction,
                            colors = IconButtonDefaults.iconButtonColors().copy(
                                containerColor = Color.Transparent,
                                contentColor = if (currentState.isShuffleEnabled) MaterialTheme.colorScheme.primary
                                else Color.White,
                            )
                        ) {
                            Icon(
                                Icons.Filled.Shuffle,
                                contentDescription = stringResource(R.string.shuffle_button_content_description)
                            )
                        }
                        Row(modifier = Modifier.padding(bottom = 32.dp)) {
                            IconButton(
                                onClick = skipPreviousAction,
                                colors = IconButtonDefaults.iconButtonColors().copy(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.White,
                                )
                            ) {
                                Icon(
                                    Icons.Filled.SkipPrevious,
                                    contentDescription = stringResource(R.string.skip_previous_button_content_description)
                                )
                            }
                            IconButton(
                                onClick = playPauseAction,
                                colors = IconButtonDefaults.iconButtonColors().copy(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                )
                            ) {
                                PlayPauseButtonIcon(currentState.isPlaying)
                            }
                            IconButton(
                                onClick = skipNextAction,
                                colors = IconButtonDefaults.iconButtonColors().copy(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.White,
                                )
                            ) {
                                Icon(
                                    Icons.Filled.SkipNext,
                                    contentDescription = stringResource(R.string.skip_next_button_content_description)
                                )
                            }
                        }
                        IconButton(
                            onClick = repeatAction,
                            colors = IconButtonDefaults.iconButtonColors().copy(
                                containerColor = Color.Transparent,
                                contentColor = if (currentState.repeatMode == Player.REPEAT_MODE_OFF) Color.White
                                else MaterialTheme.colorScheme.primary,
                            )
                        ) {
                            Icon(
                                if (currentState.repeatMode == Player.REPEAT_MODE_ONE) Icons.Filled.RepeatOne
                                else Icons.Filled.Repeat,
                                contentDescription = stringResource(R.string.repeat_button_content_description)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerSlider(
    changeProgressAction: (Long) -> Unit,
    getPositionAction: () -> Long,
    initialValue: Long,
    durationSeconds: Long,
) {
    var progress by remember { mutableFloatStateOf(initialValue.toFloat()) }
    LaunchedEffect(Unit) {
        while (isActive) {
            progress = getPositionAction().toFloat()
            delay(200)
        }
    }
    Slider(
        value = progress,
        modifier = Modifier.padding(16.dp),
        onValueChange = {
            progress = it
        },
        onValueChangeFinished = {
            changeProgressAction(progress.toLong())
        },
        valueRange = 0F..durationSeconds.toFloat(),
        thumb = {
            Box(
                Modifier
                    .size(16.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(16.dp),
                    )
            )
        },
        track = { sliderState ->
            SliderDefaults.Track(
                modifier = Modifier.height(4.dp),
                sliderState = sliderState,
                drawStopIndicator = null,
                thumbTrackGapSize = 0.dp,
                colors = SliderDefaults.colors().copy(
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = Color.White,
                ),
            )
        }
    )
}

private fun Uri.retrieveImage(context: Context): Bitmap? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, this)
    val imgBytes = retriever.embeddedPicture
    return imgBytes?.let { BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size) }
}
