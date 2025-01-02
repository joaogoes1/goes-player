package com.goesplayer.presentation.player

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.goesplayer.Music
import com.goesplayer.R

@Preview(
    showSystemUi = true,
    showBackground = true,
)
@Composable
fun Preview() {
    val context = LocalContext.current
    PlayerScreen(
        lyrics = null,
        music = Music(
            1,
            "Music teste",
            "Music teste",
            "Artist teste",
            "Album teste",
            "Genre teste",
            "Folder teste",
            Uri.EMPTY
        ),
        343,
        { Toast.makeText(context, "REPEAT ACTION", Toast.LENGTH_LONG).show() },
        { Toast.makeText(context, "REPEAT ACTION", Toast.LENGTH_LONG).show() },
        { Toast.makeText(context, "REPEAT ACTION", Toast.LENGTH_LONG).show() },
        { Toast.makeText(context, "REPEAT ACTION", Toast.LENGTH_LONG).show() },
        { Toast.makeText(context, "REPEAT ACTION", Toast.LENGTH_LONG).show() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    lyrics: String?,
    music: Music,
    durationInSeconds: Long,
    repeatAction: () -> Unit,
    jumpToPreviousAction: () -> Unit,
    playPauseAction: () -> Unit,
    jumpToNextAction: () -> Unit,
    shuffleAction: () -> Unit,
) {
    var isLyricsAppearing by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var isPlaying by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { Icons.AutoMirrored.Filled.ArrowBack },
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        }
    ) { innerPadding ->
//        if (lyrics != null)
//            Box(modifier = Modifier.border(width = 0.dp, color = Color.Transparent, shape = RoundedCornerShape(16.dp))) {
//
//            }
        Column(modifier = Modifier.padding(innerPadding)) {
            Box(
                Modifier
                    .fillMaxWidth(0.80f)
                    .aspectRatio(1f)
                    .align(Alignment.CenterHorizontally)
            ) {
                if (lyrics == null || !isLyricsAppearing)
                    Image(
                        painter = painterResource(R.mipmap.teste_album),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                else
                    Text(lyrics, modifier = Modifier.verticalScroll(rememberScrollState()))
            }
            Text(
                text = music.name,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = music.artist,
                modifier = Modifier
                    .padding(bottom = 36.dp)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleSmall,
            )
            Slider(
                value = sliderPosition,
                modifier = Modifier.padding(16.dp),
                onValueChange = { sliderPosition = it },
                valueRange = 0F..durationInSeconds.toFloat(),
                colors = SliderDefaults.colors().copy(
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White,
                ),
                thumb = {
                    Box(
                        Modifier
                            .size(24.dp)
                            .padding(4.dp)
                            .background(Color.White, RoundedCornerShape(20.dp))
                    )
                },
                track = { sliderState ->
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.White, CircleShape)
                    )
                }
            )
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                IconButton(
                    onClick = {},
                    colors = IconButtonDefaults
                        .iconButtonColors()
                        .copy(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.primary,
                        )
                ) { Icons.Filled.Shuffle }
                IconButton(
                    onClick = {},
                    colors = IconButtonDefaults
                        .iconButtonColors()
                        .copy(containerColor = Color.Transparent)
                ) { Icons.Filled.Repeat }
                IconButton(
                    onClick = {},
                    colors = IconButtonDefaults
                        .iconButtonColors()
                        .copy(containerColor = Color.Transparent)
                ) {
                    if (isPlaying)
                        Icons.Filled.Pause
                    else
                        Icons.Filled.PlayArrow
                }
                IconButton(
                    onClick = {},
                    colors = IconButtonDefaults
                        .iconButtonColors()
                        .copy(containerColor = Color.Transparent)
                ) { Icons.Filled.Repeat }
                IconButton(
                    onClick = {},
                    colors = IconButtonDefaults
                        .iconButtonColors()
                        .copy(containerColor = Color.Transparent)
                ) { Icons.Filled.Repeat }
            }
        }
    }
}