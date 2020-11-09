package com.goestech.goesplayer.view.player.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.goestech.goesplayer.data.entity.Music
import com.goestech.goesplayer.databinding.PlayerFragmentBinding
import com.goestech.goesplayer.view.MainActivity
import org.koin.android.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    private val activity: MainActivity
        get() = requireActivity() as MainActivity
//    private val viewModel: PlayerFragmentViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PlayerFragmentBinding.inflate(inflater, container, false).apply {
            initializeButtons(this)
        }.root
    }


    private fun initializeButtons(binding: PlayerFragmentBinding) {
        with(binding) {
            playerFragmentPlayButton.setOnClickListener {
                activity.playOrPause()
            }
            playerFragmentNextButton.setOnClickListener {
                activity.skipToNext()
            }
            playerFragmentPreviousButton.setOnClickListener {
                activity.skipToPrevious()
            }
            playerFragmentTotalTime.text = "IMPLEMENTA ISSO IRMÃO"
//            activity.transportControls?.currentTime.observe(viewLifecycleOwner, Observer {
//                playerFragmentCurrentTime.text = it.time.toString()
//            })
//            viewModel.music.observe(viewLifecycleOwner, Observer {
//                playerFragmentArtistName.text = it.artist
//                playerFragmentMusicName.text = it.title ?: it.displayName
//            })
            // Implementar Progress bar
        }
    }
}