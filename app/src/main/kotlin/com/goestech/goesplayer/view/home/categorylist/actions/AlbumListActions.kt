package com.goestech.goesplayer.view.home.categorylist.actions

import androidx.lifecycle.MutableLiveData
import com.goestech.goesplayer.view.home.categorylist.CategoryListType

class AlbumListActions : CategoryListViewModelActions {
    override val type: CategoryListType = CategoryListType.ALBUM

    override suspend fun loadList(list: MutableLiveData<List<String>>) {
        // TODO("Not yet implemented")
    }

    override suspend fun openItem(name: String) {
        // TODO("Not yet implemented")
    }
}