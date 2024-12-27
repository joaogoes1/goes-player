package com.goesplayer.view.home.categorylist.actions

import com.goesplayer.music.data.repository.music.MusicRepository
import com.goesplayer.view.home.categorylist.CategoryListType

class ArtistListActions(
    private val musicRepository: MusicRepository
) : CategoryListViewModelActions {
    override val type: CategoryListType = CategoryListType.ARTIST

    override suspend fun loadList(): List<String> =
        musicRepository.getAllArtists().filter {
            !it.contains("unknown", ignoreCase = true)
        }

    override suspend fun openItem(name: String) {

    }
}