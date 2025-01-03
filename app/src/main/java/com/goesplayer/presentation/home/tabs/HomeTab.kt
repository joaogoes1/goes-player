package com.goesplayer.presentation.home.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.goesplayer.AppTheme
import com.goesplayer.R

@Composable
fun HomeTab() {
    Box(modifier = Modifier.fillMaxSize())  {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Image(
                painterResource(R.mipmap.perfil_teste),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 16.dp)
                    .width(150.dp)
                    .height(150.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Text(
                stringResource(R.string.home_fragment_welcome_text),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                stringResource(R.string.home_fragment_last_music_title),
                style = MaterialTheme.typography.titleMedium,
            )
            // TODO: Load data from database. Just placeholder image and texts for now
            Row {
                Image(painterResource(R.mipmap.teste_album), contentDescription = null)
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text("Nome da música", style = MaterialTheme.typography.labelLarge)
                    Text("Nome do artiste", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
