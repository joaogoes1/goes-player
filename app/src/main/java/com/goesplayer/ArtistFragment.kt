package com.goesplayer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import com.goesplayer.presentation.home.HomeList


class ArtistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val artistsList = filterArtists()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    HomeList(
                        title = stringResource(R.string.artist_fragment_title),
                        items = artistsList,
                        onClick = { position ->
                            val intent = Intent(
                                context,
                                ResultActivity::class.java
                            )
                            intent.putExtra("name", artistsList[position])
                            intent.putExtra("type", ResultActivity.ARTISTA)
                            startActivity(intent)
                        },
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val aleatorioButton = view.findViewById<FloatingActionButton>(R.id.aleatorio)
//        aleatorioButton.setImageResource(R.drawable.ic_aleatorio)
//        aleatorioButton.setOnClickListener {
//            (activity as MainActivity).musicSrv.aleatorio = false
//            (activity as MainActivity).musicSrv.ordenarPlayList()
//            (activity as MainActivity).musicSrv.reproduzir()
//        }
    }

    private fun filterArtists() =
        MainActivity
            .todasMusicas
            .map { it.artist }
            .filter { it != "<unknown>" }
            .sortedBy { it }
            .distinct()
}