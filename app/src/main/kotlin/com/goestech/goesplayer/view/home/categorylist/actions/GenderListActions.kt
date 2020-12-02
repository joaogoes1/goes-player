package com.goestech.goesplayer.view.home.categorylist.actions

import androidx.lifecycle.MutableLiveData
import com.goestech.goesplayer.data.repository.music.MusicRepository
import com.goestech.goesplayer.view.home.categorylist.CategoryListType

class GenreListActions(
    private val musicRepository: MusicRepository
) : CategoryListViewModelActions {
    override val type: CategoryListType = CategoryListType.GENDER

    override suspend fun loadList(list: MutableLiveData<List<String>>) {
        val genres = musicRepository.getAllGenres().filter {
            !it.contains("unknown", ignoreCase = true)
        }
        list.postValue(genres)
    }

    override suspend fun openItem(name: String) {
        // TODO("Not yet implemented")
    }
}