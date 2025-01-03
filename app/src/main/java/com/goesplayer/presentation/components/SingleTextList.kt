package com.goesplayer.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleTextList(
    onClick: (Int) -> Unit = {},
    onLongClick: (Int) -> Unit = {},
    title: String,
    emptyStateMessage: String,
    items: List<String>,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (items.isNotEmpty()) {
                LazyColumn {
                    itemsIndexed(items) { index, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {
                                        onClick(index)
                                    },
                                    onLongClick = {
                                        onLongClick(index)
                                    },
                                )
                        ) {
                            Text(
                                item,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                        if (index < items.lastIndex) {
                            Spacer(Modifier.height(8.dp))
                            HorizontalDivider(
                                color = Color.DarkGray,
                                modifier = Modifier.width(64.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            } else {
                EmptyScreen(text = emptyStateMessage)
            }
        }
    }
}
