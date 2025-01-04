package com.goesplayer.presentation.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.goesplayer.R

data class DoubleTextWithAlbumItemView(
    val id: Long,
    val name: String,
    val artist: String,
    val uri: Uri,
    val thumb: Bitmap?,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DoubleTextWithAlbumArtList(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = {},
    onLongClick: (Int) -> Unit = {},
    title: String,
    emptyStateMessage: String,
    items: List<DoubleTextWithAlbumItemView>,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (items.isNotEmpty()) {

                LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
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
                            Row(modifier = Modifier.fillMaxWidth()) {
                                val retriever = MediaMetadataRetriever()
                                retriever.setDataSource(LocalContext.current, item.uri)
                                val imgBytes = retriever.embeddedPicture
                                if (imgBytes != null) {
                                    val thumb =
                                        BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size)
                                    Image(
                                        bitmap = thumb.asImageBitmap(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(48.dp)
                                    )
                                } else {
                                    Image(painterResource(R.mipmap.teste_album), null)
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(start = 8.dp),
                                    verticalArrangement = Arrangement.SpaceAround
                                ) {
                                    Text(
                                        text = item.name,
                                        maxLines = 1,
                                        style = MaterialTheme.typography.bodyLarge,
                                    )
                                    Text(
                                        text = item.artist,
                                        maxLines = 1,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                            }
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