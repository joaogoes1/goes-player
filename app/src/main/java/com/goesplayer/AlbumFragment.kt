package com.goesplayer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.goesplayer.presentation.home.HomeList
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AlbumFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val albumsList = filterAlbums()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    Box(
                      modifier = Modifier.padding(80.dp)
                    ) {
                        HomeList(
                            title = stringResource(R.string.album_fragment_title),
                            items = albumsList,
                            onClick = { position ->
                                val intent = Intent(
                                    context,
                                    ResultActivity::class.java
                                )
                                intent.putExtra("name", albumsList[position])
                                intent.putExtra("type", ResultActivity.ALBUM)
                                startActivity(intent)
                            },
                        )
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val aleatorioButton = view.findViewById<FloatingActionButton>(R.id.aleatorio)
        aleatorioButton.setImageResource(R.drawable.ic_aleatorio)
        aleatorioButton.setOnClickListener {
            (activity as MainActivity).musicSrv.aleatorio = false
            (activity as MainActivity).musicSrv.ordenarPlayList()
            (activity as MainActivity).musicSrv.reproduzir()
        }
    }

    private fun filterAlbums() =
        MainActivity
            .todasMusicas
            .map { it.album }
            .filter { it != "<unknown>" }
            .sortedBy { it }
            .distinct()
}