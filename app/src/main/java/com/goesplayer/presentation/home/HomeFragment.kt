package com.goesplayer.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.goesplayer.AppTheme
import com.goesplayer.OldMainActivity
import com.goesplayer.presentation.MainActivity

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val isMusicActive = remember { mutableStateOf(false) }
                val isMusicPlaying = remember { mutableStateOf(false) }
                AppTheme {
                    HomeScreen(
                        { (activity as MainActivity).playSong(it) },
                        (activity as MainActivity).viewModel.songList, //FIXME
                        isMusicActive,
                        isMusicPlaying,
                        (activity as MainActivity).viewModel.playlists, //FIXME
                        (activity as MainActivity).viewModel.isLoadingPlaylists, //FIXME
                    )
                }
            }
        }
    }
}
