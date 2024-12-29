package com.goesplayer;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new MainFragment();
        } else if (position == 1) {
            return new PlaylistFragment();
        } else if (position == 2) {
            return new MusicFragment();
        } else if (position == 3) {
            return new ArtistFragment();
        } else if (position == 4) {
            return new AlbumFragment();
        } else if (position == 5) {
            return new GenreFragment();
        } else {
            return new FolderFragment();
        }
    }

    @Override
    public int getCount() {
        return 7;
    }
}
