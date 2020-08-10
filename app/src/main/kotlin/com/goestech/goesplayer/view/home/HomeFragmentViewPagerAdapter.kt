package com.goestech.goesplayer.view.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.goestech.goesplayer.view.home.defaultlist.HomeDefaultListFragment
import com.goestech.goesplayer.view.home.defaultlist.HomeDefaultListViewType
import com.goestech.goesplayer.view.home.profile.ProfileFragment
import com.goestech.goesplayer.view.home.welcome.WelcomeFragment

class HomeFragmentViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> WelcomeFragment.newInstance()
            1 -> HomeDefaultListFragment.newInstance(HomeDefaultListViewType.PLAYLIST)
            2 -> HomeDefaultListFragment.newInstance(HomeDefaultListViewType.MUSIC)
            3 -> HomeDefaultListFragment.newInstance(HomeDefaultListViewType.ARTIST)
            4 -> HomeDefaultListFragment.newInstance(HomeDefaultListViewType.ALBUM)
            5 -> HomeDefaultListFragment.newInstance(HomeDefaultListViewType.GENDER)
            6 -> HomeDefaultListFragment.newInstance(HomeDefaultListViewType.FOLDER)
            7 -> ProfileFragment.newInstance()
            else -> WelcomeFragment.newInstance()
        }
}