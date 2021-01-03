package com.goestech.goesplayer.view.home.categorylist.actions

import com.goestech.goesplayer.view.home.categorylist.CategoryListType

interface CategoryListViewModelActions {
    val type: CategoryListType
    suspend fun loadList(): List<String>
    suspend fun openItem(name: String)
}