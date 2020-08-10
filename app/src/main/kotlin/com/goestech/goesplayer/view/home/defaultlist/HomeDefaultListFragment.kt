package com.goestech.goesplayer.view.home.defaultlist

import android.os.Bundle
import androidx.fragment.app.Fragment

const val HOME_DEFAULT_LIST_FRAGMENT_VIEW_TYPE = "HOME_DEFAULT_LIST_FRAGMENT_VIEW_TYPE"

class HomeDefaultListFragment : Fragment() {

    companion object {
        fun newInstance(view: HomeDefaultListViewType): HomeDefaultListFragment {
            val args = Bundle().apply {
                putSerializable(HOME_DEFAULT_LIST_FRAGMENT_VIEW_TYPE, view)
            }

            return HomeDefaultListFragment().apply {
                arguments = args
            }
        }
    }
}
