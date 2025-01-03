package com.goesplayer.presentation.player

import android.annotation.SuppressLint
import android.graphics.Bitmap
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lyrics
import androidx.compose.material.icons.filled.Repeat
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
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.goesplayer.AppTheme
import com.goesplayer.R
import com.goesplayer.data.model.Music
import com.goesplayer.presentation.widgets.PlayPauseButtonIcon

@Preview(
    showSystemUi = true,
    showBackground = true,
)
@Composable
fun Preview() {
    val context = LocalContext.current
    AppTheme {
        PlayerScreen(
            { Toast.makeText(context, "REPEAT ACTION", Toast.LENGTH_LONG).show() },
            { Toast.makeText(context, "REPEAT ACTION", Toast.LENGTH_LONG).show() },
            { Toast.makeText(context, "REPEAT ACTION", Toast.LENGTH_LONG).show() },
            { Toast.makeText(context, "REPEAT ACTION", Toast.LENGTH_LONG).show() },
            { Toast.makeText(context, "REPEAT ACTION", Toast.LENGTH_LONG).show() },
            lyrics = mockLyrics,
            isPlaying = remember { derivedStateOf { true }},
            music = remember {
                derivedStateOf {
                    Music(
                        1,
                        "Music teste",
                        "Music teste",
                        "Artist teste",
                        "Album teste",
                        "Genre teste",
                        Uri.EMPTY,
                        Uri.EMPTY,
                        343
                    )
                }
            },
            albumArt = null
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    repeatAction: () -> Unit,
    skipPreviousAction: () -> Unit,
    playPauseAction: () -> Unit,
    skipNextAction: () -> Unit,
    shuffleAction: () -> Unit,
    isPlaying: State<Boolean>,
    lyrics: String?,
    music: State<Music>,
    albumArt: Bitmap?
) {
    var isLyricsAppearing by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { Icons.AutoMirrored.Filled.ArrowBack },
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = { isLyricsAppearing = !isLyricsAppearing }) {
                        Icon(
                            Icons.Filled.Lyrics,
                            contentDescription = if (isLyricsAppearing) stringResource(
                                R.string.player_fragment_hide_lyrics_button_content_description
                            ) else stringResource(
                                R.string.player_fragment_show_lyrics_button_content_description
                            ),
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        }
    ) { _ ->
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
                if (lyrics == null || !isLyricsAppearing)
                    if (albumArt != null)
                        Image(
                            bitmap = albumArt.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .aspectRatio(1f))
                    else
                        Image(
                            painter = painterResource(R.mipmap.teste_album),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .aspectRatio(1f)
                        )
                else
                    Text(
                        lyrics, modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .verticalScroll(rememberScrollState())
                            .background(color = Color(0xFF151515))
                            .padding(8.dp)
                    )
            }
            Text(
                text = music.value.title,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = music.value.artist,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleSmall,
            )
            Slider(
                value = sliderPosition,
                modifier = Modifier.padding(16.dp),
                onValueChange = { sliderPosition = it },
                valueRange = 0F..music.value.durationInSeconds.toFloat(),
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                IconButton(
                    onClick = shuffleAction,
                    colors = IconButtonDefaults
                        .iconButtonColors()
                        .copy(
                            containerColor = Color.Transparent,
                            contentColor = Color.White,
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
                        colors = IconButtonDefaults
                            .iconButtonColors()
                            .copy(
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
                        colors = IconButtonDefaults
                            .iconButtonColors()
                            .copy(
                                containerColor = MaterialTheme.colorScheme.primary,
                            )
                    ) {
                        PlayPauseButtonIcon(isPlaying.value)
                    }
                    IconButton(
                        onClick = skipNextAction,
                        colors = IconButtonDefaults
                            .iconButtonColors()
                            .copy(
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
                    colors = IconButtonDefaults
                        .iconButtonColors()
                        .copy(
                            containerColor = Color.Transparent,
                            contentColor = Color.White,
                        )
                ) {
                    Icon(
                        Icons.Filled.Repeat,
                        contentDescription = stringResource(R.string.repeat_button_content_description)
                    )
                }
            }
        }
    }
}

// TODO: Delete this mock lyrics
const val mockLyrics = "They got loose to you\n" +
        "Here it comes\n" +
        "Oh, whoa-whoa\n" +
        "Big wheels keep on turnin'\n" +
        "Carry me home to see my kin\n" +
        "Singin' songs about the Southland\n" +
        "I miss Alabamy once again, and I think it's a sin, I said\n" +
        "Well, I heard Mr. Young sing about her\n" +
        "Well, I heard old Neil put her down\n" +
        "Well, I hope Neil Young will remember\n" +
        "A Southern man don't need him around, anyhow\n" +
        "Sweet home Alabama\n" +
        "Where the skies are so blue\n" +
        "Sweet home Alabama\n" +
        "Lord, I'm comin' home to you\n" +
        "One thing I wanna tell you\n" +
        "In Birmingham, they love the governor (boo, boo, boo!)\n" +
        "Now we all did what we could do\n" +
        "Now Watergate does not bother me, uh-uh\n" +
        "Does your conscience bother you? Tell the truth\n" +
        "Sweet home Alabama\n" +
        "Where the skies are so blue\n" +
        "Sweet home Alabama (oh, my baby)\n" +
        "Lord, I'm comin' home to you (here I come, Alabama)\n" +
        "Speak your mind\n" +
        "Ah-ah-ah (can you feel that?), Alabama\n" +
        "Ah-ah-ah, Alabama\n" +
        "Ah-ah-ah, Alabama\n" +
        "Ah-ah-ah, Alabama\n" +
        "Now Muscle Shoals has got the Swampers\n" +
        "And they've been known to pick a song or two (yes, they do)\n" +
        "Lord, they get me off so much\n" +
        "They pick me up when I'm feelin' blue, now how 'bout you?\n" +
        "Sweet home Alabama (oh)\n" +
        "Where the skies are so blue\n" +
        "Sweet home Alabama\n" +
        "Lord, I'm comin' home to you\n" +
        "Sweet home Alabama (home, sweet home, baby)\n" +
        "Where the skies are so blue (and the governor's, too)\n" +
        "Sweet home Alabama (Lord, yeah)\n" +
        "Lord, I'm comin' home to you (whoo, whoa, yeah, oh)\n" +
        "Alright, brother, now\n" +
        "Wait one minute\n" +
        "Oh-oh, sweet Alabama\n" +
        "Thank you"