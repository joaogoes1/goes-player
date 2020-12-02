package com.goestech.goesplayer.view.home.categorylist.actions

import androidx.lifecycle.MutableLiveData
import com.goestech.goesplayer.view.home.categorylist.CategoryListType

interface CategoryListViewModelActions {
    val type: CategoryListType
    suspend fun loadList(list: MutableLiveData<List<String>>)
    suspend fun openItem(name: String)
}