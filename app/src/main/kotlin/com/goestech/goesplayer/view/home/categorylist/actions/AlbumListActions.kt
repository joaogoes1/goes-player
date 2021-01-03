package com.goestech.goesplayer.view.home.categorylist.actions

import com.goestech.goesplayer.data.repository.music.MusicRepository
import com.goestech.goesplayer.view.home.categorylist.CategoryListType

class AlbumListActions(
    private val musicRepository: MusicRepository
) : CategoryListViewModelActions {
    override val type: CategoryListType = CategoryListType.ALBUM

    override suspend fun loadList(): List<String> =
        musicRepository.getAllAlbums().filter {
            it.contains("unknown")
        }


    override suspend fun openItem(name: String) {
        // TODO("Not yet implemented")
    }
}