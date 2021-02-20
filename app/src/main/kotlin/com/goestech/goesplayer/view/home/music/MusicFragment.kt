package com.goestech.goesplayer.view.home.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.goesplayer.music.data.model.Music
import com.goestech.goesplayer.databinding.MusicFragmentBinding
import com.goestech.goesplayer.view.MainActivity
import com.goestech.goesplayer.view.player.screen.PlayerFragmentDirections
import org.koin.android.viewmodel.ext.android.viewModel

class MusicFragment : Fragment(), MusicFragmentListener {

    private var binding: MusicFragmentBinding? = null
    private val viewModel: MusicViewModel by viewModel()
    private val adapter = MusicFragmentAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MusicFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            musicFragmentList.adapter = adapter
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.musics.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        viewModel.loadMusics()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun playMusic(music: Music) {
        (requireActivity() as MainActivity).playMedia(music)
        findNavController().navigate(
            PlayerFragmentDirections.openPlayerFragment()
        )
    }
}