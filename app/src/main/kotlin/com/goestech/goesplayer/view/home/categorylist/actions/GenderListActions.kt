package com.goesplayer.view.home.categorylist.actions

import com.goesplayer.music.data.repository.music.MusicRepository
import com.goesplayer.view.home.categorylist.CategoryListType

class GenreListActions(
    private val musicRepository: MusicRepository
) : CategoryListViewModelActions {
    override val type: CategoryListType = CategoryListType.GENDER

    override suspend fun loadList(): List<String> =
        musicRepository.getAllGenres().filter {
            !it.contains("unknown", ignoreCase = true)
        }

    override suspend fun openItem(name: String) {
        // TODO("Not yet implemented")
    }
}