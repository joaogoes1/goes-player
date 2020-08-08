package com.goestech.goesplayer.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.goestech.goesplayer.databinding.HomeFragmentBinding

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
    }
}
