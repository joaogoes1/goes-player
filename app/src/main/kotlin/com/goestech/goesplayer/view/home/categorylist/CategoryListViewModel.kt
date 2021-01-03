package com.goestech.goesplayer.view.home.categorylist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goestech.goesplayer.view.home.categorylist.actions.CategoryListViewModelActions
import kotlinx.coroutines.launch

class CategoryListViewModel(
    private val actions: CategoryListViewModelActions
) : ViewModel() {
    val list: MutableLiveData<List<String>> = MutableLiveData()

    fun loadList() {
        viewModelScope.launch {
            list.postValue(actions.loadList())
        }
    }
}