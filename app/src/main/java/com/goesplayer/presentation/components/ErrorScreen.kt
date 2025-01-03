package com.goesplayer.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorScreen(
    retryAction: (() -> Unit)?
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(
                text = "Algo deu errado",
                style = MaterialTheme.typography.titleLarge,
            )

            if (retryAction != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = retryAction) {
                    Text(
                        "Tente novamente",
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}