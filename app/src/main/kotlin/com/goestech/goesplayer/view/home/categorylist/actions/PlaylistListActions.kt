package com.goestech.goesplayer.view.home.categorylist.actions

import com.goesplayer.music.data.repository.playlist.PlaylistRepository
import com.goestech.goesplayer.view.home.categorylist.CategoryListType

class PlaylistListActions(
    private val playlistRepository: PlaylistRepository
) : CategoryListViewModelActions {
    override val type: CategoryListType = CategoryListType.PLAYLIST

    override suspend fun loadList(): List<String> = playlistRepository.getAllPlaylists()

    override suspend fun openItem(name: String) {
        // TODO("Not yet implemented")
    }
}