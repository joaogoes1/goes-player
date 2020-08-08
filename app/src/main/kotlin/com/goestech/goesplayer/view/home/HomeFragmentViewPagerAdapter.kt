package com.goestech.goesplayer.view.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeFragmentViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int =

    override fun createFragment(position: Int): Fragment {
        TODO("Not yet implemented")
    }

}