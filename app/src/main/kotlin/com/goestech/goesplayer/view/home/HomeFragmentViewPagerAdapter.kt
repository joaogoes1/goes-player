package com.goestech.goesplayer.view.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.goestech.goesplayer.view.home.album.AlbumFragment
import com.goestech.goesplayer.view.home.artist.ArtistFragment
import com.goestech.goesplayer.view.home.folder.FolderFragment
import com.goestech.goesplayer.view.home.gender.GenderFragment
import com.goestech.goesplayer.view.home.music.MusicFragment
import com.goestech.goesplayer.view.home.playlist.PlaylistFragment
import com.goestech.goesplayer.view.home.welcome.WelcomeFragment

class HomeFragmentViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> WelcomeFragment()
            1 -> MusicFragment()
            2 -> PlaylistFragment()
            3 -> ArtistFragment()
            4 -> AlbumFragment()
            5 -> GenderFragment()
            6 -> FolderFragment()
            else -> WelcomeFragment()
        }
}