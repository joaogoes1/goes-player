package com.goesplayer.presentation.player

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.media3.session.MediaController
import androidx.navigation.compose.rememberNavController
import com.goesplayer.AppTheme
import com.goesplayer.data.model.Music
import com.goesplayer.presentation.MainActivity

class PlayerFragment : Fragment() {
    private var controller: MediaController? = null

    private fun pauseMusic() {
        controller?.let {
            if (it.isPlaying)
                it.pause()
            else
                it.play()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        controller = (activity as MainActivity).viewModel.controller
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val music = (activity as MainActivity).viewModel.currentMusic.observeAsState()

                music.value?.let {
                    AppTheme {
                        music.value?.let {
                            PlayerScreen(
                                {},
                                {},
                                ::pauseMusic,
                                {},
                                {},
                                (activity as MainActivity).viewModel.isPlaying,
                                null,
                                it,
                                retrieveImage(it),
                                rememberNavController(),
                            )
                        }
                    }
                }
            }
        }
    }

    private fun retrieveImage(music: Music): Bitmap? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, music.albumArtUri)
        val imgBytes = retriever.embeddedPicture
        return imgBytes?.let { BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size) }
    }
}