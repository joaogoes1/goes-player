package com.goestech.goesplayer.view.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.goestech.goesplayer.view.home.categorylist.CategoryListFragment
import com.goestech.goesplayer.view.home.categorylist.CategoryListType
import com.goestech.goesplayer.view.home.music.MusicFragment
import com.goestech.goesplayer.view.home.welcome.WelcomeFragment

class HomeFragmentViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> WelcomeFragment()
            1 -> MusicFragment()
            2 -> CategoryListFragment.newInstance(CategoryListType.PLAYLIST)
            3 -> CategoryListFragment.newInstance(CategoryListType.ARTIST)
            4 -> CategoryListFragment.newInstance(CategoryListType.ALBUM)
            5 -> CategoryListFragment.newInstance(CategoryListType.GENDER)
            6 -> CategoryListFragment.newInstance(CategoryListType.FOLDER)
            else -> throw IllegalStateException("Invalid position")
        }
}