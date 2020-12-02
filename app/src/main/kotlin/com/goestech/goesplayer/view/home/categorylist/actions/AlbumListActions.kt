package com.goestech.goesplayer.view.home.categorylist.actions

import androidx.lifecycle.MutableLiveData
import com.goestech.goesplayer.data.repository.music.MusicRepository
import com.goestech.goesplayer.view.home.categorylist.CategoryListType

class AlbumListActions(
    private val musicRepository: MusicRepository
) : CategoryListViewModelActions {
    override val type: CategoryListType = CategoryListType.ALBUM

    override suspend fun loadList(list: MutableLiveData<List<String>>) {
        val albums = musicRepository.getAllAlbums().filter {
            it.contains("unknown")
        }
        list.postValue(albums)
    }

    override suspend fun openItem(name: String) {
        // TODO("Not yet implemented")
    }
}