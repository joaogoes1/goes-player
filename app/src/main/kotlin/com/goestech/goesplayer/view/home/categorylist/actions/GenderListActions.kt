package com.goestech.goesplayer.view.home.categorylist.actions

import androidx.lifecycle.MutableLiveData
import com.goestech.goesplayer.view.home.categorylist.CategoryListType

class GenderListActions : CategoryListViewModelActions {
    override val type: CategoryListType = CategoryListType.GENDER

    override suspend fun loadList(list: MutableLiveData<List<String>>) {
        // TODO("Not yet implemented")
    }

    override suspend fun openItem(name: String) {
        // TODO("Not yet implemented")
    }
}