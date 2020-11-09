package com.goestech.goesplayer.view.player.bottombar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.goestech.goesplayer.databinding.BottomBarPlayerFragmentBinding

class BottomBarPlayerFragment : Fragment() {

    private lateinit var binding: BottomBarPlayerFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomBarPlayerFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            bottomBarPlayerContainer.setOnClickListener {
                // TODO: Open player
            }
        }
        return binding.root
    }
}