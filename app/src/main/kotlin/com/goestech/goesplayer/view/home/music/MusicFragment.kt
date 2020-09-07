package com.goestech.goesplayer.view.home.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.goestech.goesplayer.bussiness.model.MusicModel
import com.goestech.goesplayer.databinding.MusicFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MusicFragment : Fragment(), MusicFragmentListener {

    private var binding: MusicFragmentBinding? = null
    private val viewModel: MusicViewModel by viewModel()
    private val adapter = MusicFragmentAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MusicFragmentBinding.inflate(inflater, container, false).apply {
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

    override fun playMusic(music: MusicModel) {
        viewModel.playMusic(music)
    }
}