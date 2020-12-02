package com.goestech.goesplayer.view.home.categorylist.actions

import androidx.lifecycle.MutableLiveData
import com.goestech.goesplayer.data.repository.music.MusicRepository
import com.goestech.goesplayer.view.home.categorylist.CategoryListType

class FolderListActions(
    private val musicRepository: MusicRepository
) : CategoryListViewModelActions {
    override val type: CategoryListType = CategoryListType.FOLDER

    override suspend fun loadList(list: MutableLiveData<List<String>>) {
        val folders = musicRepository.getAllFolders().filter {
            !it.contains("unknown", ignoreCase = true)
        }
        list.postValue(folders)
    }

    override suspend fun openItem(name: String) {
        // TODO("Not yet implemented")
    }
}