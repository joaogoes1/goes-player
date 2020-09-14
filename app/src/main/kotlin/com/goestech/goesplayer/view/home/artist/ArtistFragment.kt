package com.goestech.goesplayer.view.home.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.goestech.goesplayer.data.entity.Music
import com.goestech.goesplayer.databinding.ArtistFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel

class ArtistFragment : Fragment(), ArtistFragmentListener {

    private var binding: ArtistFragmentBinding? = null
    private val viewModel: ArtistViewModel by viewModel()
    private val adapter = ArtistFragmentAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ArtistFragmentBinding.inflate(inflater, container, false).apply {
            artistFragmentList.adapter = adapter
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.artists.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        viewModel.loadArtist()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun openArtist(artist: String) {
        TODO("Not yet implemented")
    }
}