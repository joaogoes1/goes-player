package com.goesplayer.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeList(
    onClick: (index: Int) -> Unit,
    title: String,
    items: List<String>,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyColumn {
            itemsIndexed(items) { index, item ->
                TextButton(onClick = {
                    onClick(index)
                }
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}