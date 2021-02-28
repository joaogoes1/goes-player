package com.goestech.goesplayer.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.goestech.goesplayer.R
import com.goestech.goesplayer.databinding.HomeFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private val viewPager: ViewPager2 by lazy { binding.homeFragmentViewPager }
    private lateinit var homeFragmentViewPagerAdapter: HomeFragmentViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        homeFragmentViewPagerAdapter = HomeFragmentViewPagerAdapter(this)
        viewPager.adapter = homeFragmentViewPagerAdapter
        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            val icon = when (position) {
                0 -> R.drawable.ic_home
                1 -> R.drawable.ic_music
                2 -> R.drawable.ic_playlist
                3 -> R.drawable.ic_artist
                4 -> R.drawable.ic_album
                5 -> R.drawable.ic_genre
                6 -> R.drawable.ic_folder
                else -> R.drawable.ic_home
            }
            tab.icon = ContextCompat.getDrawable(requireContext(), icon)
        }.attach()
    }
}
