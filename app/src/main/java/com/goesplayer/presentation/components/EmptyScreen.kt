package com.goesplayer.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge.copy(color = Color.Gray),
            textAlign = TextAlign.Center,
        )
    }
}