package com.goestech.goesplayer.view.home.categorylist.actions

import com.goesplayer.music.data.repository.music.MusicRepository
import com.goestech.goesplayer.view.home.categorylist.CategoryListType

class FolderListActions(
    private val musicRepository: MusicRepository
) : CategoryListViewModelActions {
    override val type: CategoryListType = CategoryListType.FOLDER

    override suspend fun loadList(): List<String> =
        musicRepository.getAllFolders().filter {
            !it.contains("unknown", ignoreCase = true)
        }

    override suspend fun openItem(name: String) {
        // TODO("Not yet implemented")
    }
}