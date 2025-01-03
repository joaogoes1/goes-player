package com.goesplayer.presentation.home.tabs

import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap.Fold
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.goesplayer.OldMainActivity.todasMusicas
import com.goesplayer.R
import com.goesplayer.ResultActivity
import com.goesplayer.presentation.home.HomeList

@Composable
fun FolderTab(context: Context) {
    val foldersList = filterfolders()
    Box(modifier = Modifier.fillMaxSize()) {
        HomeList(
            title = stringResource(R.string.folder_fragment_title),
            items = foldersList,
            onClick = { position ->
                val intent = Intent(
                    context,
                    ResultActivity::class.java
                )
                intent.putExtra("name", foldersList[position])
                intent.putExtra("type", ResultActivity.FOLDER)
                context.startActivity(intent)
            },
        )
    }
}

private fun filterfolders(): List<String> =
    todasMusicas
        .mapNotNull { it.folder }
        .filter { it != "<unknown>" }
        .sortedBy { it }
        .distinct()
